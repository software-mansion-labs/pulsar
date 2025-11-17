package com.swmansion.pulsarapp.types

import android.os.Build
import android.os.VibrationEffect
import android.util.Log
import androidx.annotation.RequiresApi
import com.swmansion.pulsarapp.TAG
import kotlin.math.roundToInt

const val MIN_TIME: Long = 20
const val MAX_FREQUENCY = 160

data class Preset(
  val name: String,
  val bars: ArrayList<Bar>? = null,
  val controlPoints: ArrayList<EnvelopePoint>? = null,
) {
  @RequiresApi(Build.VERSION_CODES.O) val vibrationEffect = createVibrationEffect()

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createVibrationEffect(): VibrationEffect? {
    val barsWaveform =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
        createWaveformFromPoints(convertBarsToPoints(bars))
      } else {
        createWaveformFromBars()
      }

    val pointsWaveform = createWaveformFromPoints(controlPoints)

    barsWaveform?.let {
      Log.i(TAG, "Vibration created based on bars.")
      return barsWaveform
    }

    pointsWaveform?.let {
      Log.i(TAG, "Vibration created based on points.")
      return pointsWaveform
    }

    Log.w(TAG, "Vibration creation failed.")
    return null
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun createWaveformFromBars(): VibrationEffect? {
    if (bars == null) {
      return null
    } else {
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
        amplitudes += (currBar.amplitude * 255).roundToInt()
      }

      return VibrationEffect.createWaveform(timings, amplitudes, -1)
    }
  }

  fun createWaveformFromPoints(points: ArrayList<EnvelopePoint>?): VibrationEffect? {
    if (points == null) {
      return null
    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.BAKLAVA) {
      Log.w(
        TAG,
        "Failed to create waveform. Control points waveforms are supported only on Android 16",
      )
      return null
    } else {
      val builder = VibrationEffect.WaveformEnvelopeBuilder()
      val n = points.size

      for (i in 1..n - 1) {
        val prevPoint = points[i - 1]
        val currPoint = points[i]

        // handle start from non zero value
        if (i == 1 && prevPoint.intensity.toInt() != 0) {
          builder.addControlPoint(prevPoint.intensity, prevPoint.sharpness * MAX_FREQUENCY, MIN_TIME)
        }

        val pointsTimeDiff = currPoint.relativeTime - prevPoint.relativeTime
        val duration =
          if (pointsTimeDiff > 0) pointsTimeDiff
          else MIN_TIME // TODO: use device values for MIN_TIME etc.

        builder.addControlPoint(currPoint.intensity, currPoint.sharpness * MAX_FREQUENCY, duration)
      }

      return builder.build()
    }
  }

  fun convertBarsToPoints(bars: ArrayList<Bar>?): ArrayList<EnvelopePoint>? {
    if (bars == null) {
      return null
    }

    val points = ArrayList<EnvelopePoint>()
    val n = bars.size

    for (i in 0..n - 1) {
      val prevBar = if (i == 0) null else bars[i - 1]
      val currBar = bars[i]

      // add empty interval at the beginning if first bar do not start at 0
      if (i == 0 && currBar.x1.toInt() != 0) {
        addIntervalEdgesToPoints(points, 0f, currBar.frequency, 0, currBar.x1)
      }

      // add empty interval between bars if they are not next to each other
      if (prevBar != null && prevBar.x2 != currBar.x1) {
        addIntervalEdgesToPoints(points, 0f, currBar.frequency, prevBar.x2, currBar.x1)
      }

      // add bar interval
      addIntervalEdgesToPoints(points, currBar.amplitude, currBar.frequency, currBar.x1, currBar.x2)
    }

    return points
  }

  fun addIntervalEdgesToPoints(
    points: ArrayList<EnvelopePoint>,
    intensity: Float,
    sharpness: Float,
    relativeTime1: Long,
    relativeTime2: Long,
  ) {
    points += EnvelopePoint(intensity, sharpness, relativeTime1)
    points += EnvelopePoint(intensity, sharpness, relativeTime2)
  }
}
