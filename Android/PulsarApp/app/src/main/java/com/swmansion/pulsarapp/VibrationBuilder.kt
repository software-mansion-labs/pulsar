package com.swmansion.pulsarapp

import android.os.Build
import android.os.VibrationEffect
import android.os.vibrator.VibratorEnvelopeEffectInfo
import android.os.vibrator.VibratorFrequencyProfile
import android.util.Log
import androidx.annotation.RequiresApi
import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.EnvelopePoint
import com.swmansion.pulsarapp.types.Preset
import kotlin.collections.forEach
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

const val MAX_AMPLITUDE = 255
const val STEPS_PER_100_MS = 20

data class CreateVibrationEffectProps(
  val frequencyProfile: VibratorFrequencyProfile? = null,
  val envelopeInfo: VibratorEnvelopeEffectInfo,
)

class VibrationBuilder {
  fun createVibrationEffect(
    preset: Preset,
    props: CreateVibrationEffectProps? = null,
  ): VibrationEffect? {
    val (_, bars, points) = preset

    if (bars == null && points == null) {
      Log.w(TAG, "Vibration creation failed. No data in preset.")
      return null
    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      Log.w(TAG, "Vibration not supported on version before 26 yet.") // TODO: handle somehow
      return null
    } else {
      val barsWaveform = bars?.let { createWaveformFromBars(it, props) }
      val pointsWaveform = points?.let { createWaveformFromPoints(it, props) }

      if (barsWaveform != null) {
        Log.i(TAG, "Vibration created based on bars.")
        return barsWaveform
      } else if (pointsWaveform != null) {
        Log.i(TAG, "Vibration created based on points.")
        return pointsWaveform
      } else {
        Log.w(TAG, "Vibration creation failed.")
        return null
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createWaveformFromBars(
    bars: ArrayList<Bar>,
    props: CreateVibrationEffectProps?,
  ): VibrationEffect {
    return if (supportAndroid36() && props != null) {
      val points = convertBarsToPoints(bars)
      createEnvelopeWaveform(points, props)
    } else createWaveform(bars)
  }

  private fun createWaveformFromPoints(
    points: ArrayList<EnvelopePoint>,
    props: CreateVibrationEffectProps?,
  ): VibrationEffect? {
    return if (supportAndroid36() && props != null) createEnvelopeWaveform(points, props)
    else createApproximateWaveform(points)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createWaveform(bars: ArrayList<Bar>): VibrationEffect {
    var timings = longArrayOf()
    var amplitudes = intArrayOf()

    val n = bars.size
    for (i in 0..n - 1) {
      val prevBar = if (i == 0) null else bars[i - 1]
      val currBar = bars[i]

      if (prevBar != null && prevBar.x2 != currBar.x1) {
        // add pause between bars
        val pauseDuration = currBar.x1 - prevBar.x2
        timings += pauseDuration
        amplitudes += 0
      }

      val currBarDuration = currBar.x2 - currBar.x1
      timings += currBarDuration
      amplitudes += (currBar.amplitude * MAX_AMPLITUDE).roundToInt()
    }

    return VibrationEffect.createWaveform(timings, amplitudes, -1)
  }

  @RequiresApi(Build.VERSION_CODES.BAKLAVA)
  private fun createEnvelopeWaveform(
    points: ArrayList<EnvelopePoint>,
    props: CreateVibrationEffectProps,
  ): VibrationEffect {
    val controlPoints = getControlPoints(points, props)

    return props.frequencyProfile?.let {
      val builder = VibrationEffect.WaveformEnvelopeBuilder()
      controlPoints.forEach { builder.addControlPoint(it.intensity, it.sharpness, it.relativeTime) }
      builder.build()
    }
      ?: run {
        val builder = VibrationEffect.BasicEnvelopeBuilder()
        controlPoints.forEach {
          builder.addControlPoint(it.intensity, it.sharpness, it.relativeTime)
        }
        builder.build()
      }
  }

  @RequiresApi(Build.VERSION_CODES.BAKLAVA)
  private fun getControlPoints(
    points: ArrayList<EnvelopePoint>,
    props: CreateVibrationEffectProps,
  ): ArrayList<EnvelopePoint> {
    val controlPoints: ArrayList<EnvelopePoint> = arrayListOf()
    val n = points.size
    val minDuration = props.envelopeInfo.minControlPointDurationMillis

    for (i in 0..n - 1) {
      val currPoint = points[i]

      if (i == 0) {
        // handle start from non zero amplitude
        if (currPoint.intensity != 0f) {
          controlPoints +=
            createControlPoint(props, currPoint.intensity, currPoint.sharpness, minDuration)
        }
      } else {
        // handle transition between points
        val prevPoint = points[i - 1]
        val pointsTimeDiff = currPoint.relativeTime - prevPoint.relativeTime
        val duration = if (pointsTimeDiff > 0) pointsTimeDiff else minDuration

        controlPoints +=
          createControlPoint(props, currPoint.intensity, currPoint.sharpness, duration)
      }
    }

    return controlPoints
  }

  @RequiresApi(Build.VERSION_CODES.BAKLAVA)
  private fun createControlPoint(
    props: CreateVibrationEffectProps,
    intensity: Float,
    sharpness: Float,
    duration: Long,
  ): EnvelopePoint {
    val envelopeInfo = props.envelopeInfo
    val frequencyProfile = props.frequencyProfile

    val minDuration = envelopeInfo.minControlPointDurationMillis
    val maxDuration = envelopeInfo.maxControlPointDurationMillis
    val adjustedDuration = max(min(duration, maxDuration), minDuration)

    val adjustedFrequency =
      frequencyProfile?.let {
        val minFrequencyHz = it.minFrequencyHz
        val maxFrequencyHz = it.maxFrequencyHz
        sharpness * (maxFrequencyHz - minFrequencyHz) + minFrequencyHz
      } ?: sharpness

    return EnvelopePoint(intensity, adjustedFrequency, adjustedDuration)
  }

  fun convertBarsToPoints(bars: ArrayList<Bar>): ArrayList<EnvelopePoint> {
    val points = ArrayList<EnvelopePoint>()
    val n = bars.size

    // TODO: better validation
    val validBars = bars.filter { it.amplitude != 0f }

    // create empty interval at the beginning
    if (validBars.isNotEmpty() && validBars[0].x1 != 0L) {
      points += EnvelopePoint(0f, 0f, 0)
    }

    for (i in 0..n - 1) {
      val currBar = validBars[i]

      if (i == 0 || validBars[i - 1].x2 != currBar.x1) {
        points += EnvelopePoint(0f, currBar.frequency, currBar.x1)
      }

      points += EnvelopePoint(currBar.amplitude, currBar.frequency, currBar.x1)
      points += EnvelopePoint(currBar.amplitude, currBar.frequency, currBar.x2)

      if (i == n - 1 || currBar.x2 != validBars[i + 1].x1) {
        points += EnvelopePoint(0f, currBar.frequency, currBar.x2)
      }
    }

    return points
  }

  private fun supportAndroid36(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA
  }

  //  @RequiresApi(Build.VERSION_CODES.O)
  //  private fun createApproximateWaveform(points: ArrayList<EnvelopePoint>): VibrationEffect? {
  //    var timings = longArrayOf()
  //    var amplitudes = intArrayOf()
  //
  //    val n = points.size
  //    for (i in 1..n - 1) {
  //      val currPoint = points[i]
  //      val prevPoint = points[i - 1]
  //
  //      // skip vertical lines
  //      if (prevPoint.intensity == currPoint.intensity) {
  //        val duration = currPoint.relativeTime - prevPoint.relativeTime
  //        timings += duration
  //        amplitudes += (currPoint.intensity * MAX_AMPLITUDE).roundToInt()
  //      } else if (prevPoint.relativeTime != currPoint.relativeTime) {
  //        val step = 5
  //        val startIntensity = (prevPoint.intensity * MAX_AMPLITUDE).roundToInt()
  //        val endIntensity = (currPoint.intensity * MAX_AMPLITUDE).roundToInt()
  //
  //        val steps = abs((endIntensity - startIntensity) / step)
  //        val stepDuration = abs((currPoint.relativeTime - prevPoint.relativeTime) / steps)
  //
  //        val isAscending = prevPoint.intensity < currPoint.intensity
  //
  //        for (i in 0..steps - 1) {
  //          timings += stepDuration
  //          val amplitude = if (isAscending) startIntensity + step * i else startIntensity - step
  // * i
  //          amplitudes += amplitude
  //        }
  //      }
  //    }
  //
  //    return VibrationEffect.createWaveform(timings, amplitudes, -1)
  //  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createApproximateWaveform(points: ArrayList<EnvelopePoint>): VibrationEffect? {
    var timings = longArrayOf()
    var amplitudes = intArrayOf()

    val n = points.size
    for (i in 1..n - 1) {
      val currPoint = points[i]
      val prevPoint = points[i - 1]

      // skip vertical lines
      if (prevPoint.intensity == currPoint.intensity) {
        val duration = currPoint.relativeTime - prevPoint.relativeTime
        timings += duration
        amplitudes += (currPoint.intensity * MAX_AMPLITUDE).roundToInt()
      } else if (prevPoint.relativeTime != currPoint.relativeTime) {
        val intervalDuration = currPoint.relativeTime - prevPoint.relativeTime

        val startIntensity = prevPoint.intensity
        val endIntensity = currPoint.intensity

        val stepDuration = intervalDuration * STEPS_PER_100_MS / 100
        val steps = intervalDuration / stepDuration
        val stepValue = abs(startIntensity - endIntensity) / steps

        val isAscending = startIntensity < endIntensity

        for (i in 0..steps - 1) {
          timings += stepDuration
          val amplitude =
            if (isAscending) startIntensity + stepValue * i else startIntensity - stepValue * i
          amplitudes += (amplitude * MAX_AMPLITUDE).roundToInt()
        }
      }
    }
    return VibrationEffect.createWaveform(timings, amplitudes, -1)
  }
}
