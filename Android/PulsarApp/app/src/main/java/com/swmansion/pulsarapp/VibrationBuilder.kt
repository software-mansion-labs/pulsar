package com.swmansion.pulsarapp

import android.os.Build
import android.os.VibrationEffect
import android.os.vibrator.VibratorEnvelopeEffectInfo
import android.os.vibrator.VibratorFrequencyProfile
import android.util.Log
import androidx.annotation.RequiresApi
import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.ControlPoint
import com.swmansion.pulsarapp.types.Point
import com.swmansion.pulsarapp.types.Preset
import kotlin.collections.forEach
import kotlin.collections.plusAssign
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.roundToLong

const val MAX_AMPLITUDE = 255
const val STEPS_PER_100_MS = 30
const val DEFAULT_SHARPNESS = 1f

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
      if (bars != null && points != null) {
        val complexWaveform = createComplexWaveform(points, bars, props)

        Log.i(TAG, "Complex vibration created based on bars and points.")
        return complexWaveform
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

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createWaveformFromPoints(
    points: ArrayList<Point>,
    props: CreateVibrationEffectProps?,
  ): VibrationEffect? {
    return if (supportAndroid36() && props != null) createEnvelopeWaveform(points, props)
    else {
      val bars = convertPointsToBars(points)
      createWaveform(bars)
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createComplexWaveform(
    points: ArrayList<Point>,
    bars: ArrayList<Bar>,
    props: CreateVibrationEffectProps?,
  ): VibrationEffect? {
    val mergedPoints = mergePointsAndBars(bars, points)
    return createWaveformFromPoints(mergedPoints, props)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createWaveform(bars: ArrayList<Bar>): VibrationEffect {
    var timings = longArrayOf()
    var amplitudes = intArrayOf()

    printBarsToPlot(bars)

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
      amplitudes += (currBar.intensity * MAX_AMPLITUDE).roundToInt()
    }

    return VibrationEffect.createWaveform(timings, amplitudes, -1)
  }

  @RequiresApi(Build.VERSION_CODES.BAKLAVA)
  private fun createEnvelopeWaveform(
    points: ArrayList<Point>,
    props: CreateVibrationEffectProps,
  ): VibrationEffect {
    val controlPoints = getControlPoints(points, props)
    val p = convertControlPointToPoints(controlPoints)

    Log.i(TAG, "----------- POINTS -----------")
    printPointsToPlot(points)
    Log.i(TAG, "----------- CONTROL POINTS -----------")
    printPointsToPlot(p)
    Log.i(TAG, "--------------------------------------")

    return props.frequencyProfile?.let {
      val builder = VibrationEffect.WaveformEnvelopeBuilder()
      controlPoints.forEach { builder.addControlPoint(it.intensity, it.sharpness, it.duration) }
      builder.build()
    }
      ?: run {
        val builder = VibrationEffect.BasicEnvelopeBuilder()
        controlPoints.forEach { builder.addControlPoint(it.intensity, it.sharpness, it.duration) }
        builder.build()
      }
  }

  @RequiresApi(Build.VERSION_CODES.BAKLAVA)
  private fun getControlPoints(
    points: ArrayList<Point>,
    props: CreateVibrationEffectProps,
  ): ArrayList<ControlPoint> {
    val controlPoints = ArrayList<ControlPoint>()
    val n = points.size
    val minDuration = props.envelopeInfo.minControlPointDurationMillis

    for (i in 0..n - 1) {
      val currPoint = points[i]

      if (i == 0) {
        // handle start from non zero intensity
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
  ): ControlPoint {
    val envelopeInfo = props.envelopeInfo
    val frequencyProfile = props.frequencyProfile

    val minDuration = envelopeInfo.minControlPointDurationMillis
    val maxDuration = envelopeInfo.maxControlPointDurationMillis
    val adjustedDuration = max(min(duration, maxDuration), minDuration)

    val adjustedSharpness =
      frequencyProfile?.let {
        sharpness * (it.maxFrequencyHz - it.minFrequencyHz) + it.minFrequencyHz
      } ?: sharpness

    return ControlPoint(intensity, adjustedSharpness, adjustedDuration)
  }

  fun convertBarsToPoints(bars: ArrayList<Bar>): ArrayList<Point> {
    val points = ArrayList<Point>()
    val n = bars.size

    // TODO: better validation
    val validBars = bars.filter { it.intensity != 0f }

    // create empty interval at the beginning
    if (validBars.isNotEmpty() && validBars[0].x1 != 0L) {
      points += Point(0f, 0f, 0)
    }

    for (i in 0..n - 1) {
      val currBar = validBars[i]

      if (i == 0 || validBars[i - 1].x2 != currBar.x1) {
        points += Point(0f, currBar.sharpness, currBar.x1)
      }

      points += Point(currBar.intensity, currBar.sharpness, currBar.x1)
      points += Point(currBar.intensity, currBar.sharpness, currBar.x2)

      if (i == n - 1 || currBar.x2 != validBars[i + 1].x1) {
        points += Point(0f, currBar.sharpness, currBar.x2)
      }
    }

    return points
  }

  private fun convertPointsToBars(points: ArrayList<Point>): ArrayList<Bar> {
    val bars = ArrayList<Bar>()

    val n = points.size
    for (i in 1..n - 1) {
      val currPoint = points[i]
      val prevPoint = points[i - 1]

      // when prevPoint.relativeTime == currPoint.relativeTime skip (vertical lines)

      if (prevPoint.intensity == currPoint.intensity) {
        bars +=
          Bar(
            prevPoint.relativeTime,
            currPoint.relativeTime,
            currPoint.intensity,
            DEFAULT_SHARPNESS, // sharpness of this bar will never be used
          )
      } else if (prevPoint.relativeTime != currPoint.relativeTime) {
        val intervalDuration = currPoint.relativeTime - prevPoint.relativeTime

        val startIntensity = prevPoint.intensity
        val endIntensity = currPoint.intensity

        val steps = (intervalDuration * STEPS_PER_100_MS) / 100
        val stepDuration = intervalDuration / steps
        val stepValue = abs(startIntensity - endIntensity) / steps

        val isAscending = startIntensity < endIntensity

        for (i in 0..steps - 1) {
          val startTime = prevPoint.relativeTime + stepDuration * i
          val endTime = if (i < steps - 1) startTime + stepDuration else currPoint.relativeTime
          val intensity =
            if (isAscending) startIntensity + stepValue * i else startIntensity - stepValue * i

          bars += Bar(startTime, endTime, intensity, DEFAULT_SHARPNESS)
        }
      }
    }

    return bars
  }

  private fun supportAndroid36(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA
  }

  fun mergePointsAndBars(bars: ArrayList<Bar>, points: ArrayList<Point>): ArrayList<Point> {
    val barsWithinLineMap = getBarsWithinLineMap(points, bars)
    val nLinePoints = points.size

    var mergedPoints = ArrayList<Point>()

    for (i in 1..nLinePoints - 1) {
      val linePoint1 = points[i - 1]
      val linePoint2 = points[i]

      val barsWithinLine = barsWithinLineMap[linePoint1]!!
      var linePoints = getPointsOnTheLine(linePoint1, linePoint2, barsWithinLine)

      if (i != 1){
        val n = linePoints.size
        linePoints = ArrayList(linePoints.subList(1 , n)) // delete unnecesary point
      }

      mergedPoints.addAll(linePoints)
    }

    val toDelete = ArrayList<Point>()
    val nP = mergedPoints.size
    for (i in 0 .. nP-1){
      if(i != 0 && i != nP - 1){
        val prev = mergedPoints[i-1]
        val curr = mergedPoints[i]
        val next = mergedPoints[i+1]

        if(prev.relativeTime == curr.relativeTime && curr.relativeTime == next.relativeTime){
          toDelete.add(curr)
        }
      }
    }

    mergedPoints.removeAll(toDelete)
    mergedPoints = ArrayList(mergedPoints.distinct())

    removeDuplicats(mergedPoints)

    Log.i(TAG, "points: $mergedPoints")
    Log.i(TAG, "size: ${mergedPoints.size}")

    return mergedPoints
  }

  private fun removeDuplicats(mergedPoints: ArrayList<Point>) {
    val toDelete = ArrayList<Point>()
    val nP = mergedPoints.size
    for (i in 0 .. nP-1){
      if(i != 0 && i != nP - 1){
        val prev = mergedPoints[i-1]
        val curr = mergedPoints[i]
        val next = mergedPoints[i+1]

        if(prev.intensity == curr.intensity && curr.intensity == next.intensity){
          toDelete.add(curr)
        }
      }
    }

    mergedPoints.removeAll(toDelete)
  }

  fun getPointsOnTheLine(
    linePoint1: Point,
    linePoint2: Point,
    bars: ArrayList<Bar>,
  ): ArrayList<Point> {
    var points = arrayListOf<Point>()
    points += linePoint1

    val (a, b) = getLineParameters(linePoint1, linePoint2)

    val nBars = bars.size
    for (j in 0..nBars - 1) {
      val bar = bars[j]

      getBarIntersectionPoints(a, b, bar)?.let {
        val (intersection1, intersectionHorizontal, intersection2) = it
        val (barPoint1, barPoint2) = getBarPoints(bar)
        var barPoints: ArrayList<Point>? = null

        if (intersectionHorizontal != null) {
          if (intersection1 != null) {
            barPoints = arrayListOf(intersection1, barPoint1, intersectionHorizontal)
          } else if (intersection2 != null) {
            barPoints = arrayListOf(intersectionHorizontal, barPoint2, intersection2)
          }
        } else if (intersection1 != null && intersection2 != null) {
          barPoints = arrayListOf(intersection1, barPoint1, barPoint2, intersection2)
        } else {
          Log.i(TAG, "bar ${bar.x1}-${bar.x2} is under line - shouldn't happen")
        }

        barPoints?.forEach { p -> points.add(p) }
      }
    }

    points += linePoint2

    points = ArrayList(points.distinct())
    removeBecauseOfTriples(points)

    Log.i(TAG, "MY POINTS: ${points}")

    return points
  }

  fun removeBecauseOfTriples(points: ArrayList<Point>){
    val toDelete = ArrayList<Point>()

    val nPoints = points.size
    points.forEachIndexed { index, point ->
      if (index != 0 && index != nPoints - 1) {
        val prevPoint = points[index - 1]
        val currPoint = points[index]
        val nextPoint = points[index + 1]

        if (
          prevPoint.relativeTime == currPoint.relativeTime &&
          currPoint.relativeTime == nextPoint.relativeTime
        ) {
          toDelete.add(currPoint)
        }
      }
    }
    points.removeAll(toDelete)
  }

  fun getBarsWithinLineMap(
    points: ArrayList<Point>,
    bars: ArrayList<Bar>,
  ): Map<Point, ArrayList<Bar>> {
    val barsWithinLineMap = mutableMapOf<Point, ArrayList<Bar>>()
    val n = points.size
    var currBarIndex = 0

    for (i in 1..n - 1) {
      val prevPoint = points[i - 1]
      val currPoint = points[i]
      val lineBars = ArrayList<Bar>()

      for (j in currBarIndex..bars.size - 1) {
        val bar = bars[j]
        if (prevPoint.relativeTime <= bar.x1 && bar.x2 <= currPoint.relativeTime) {
          lineBars += bar
          currBarIndex += 1
        } else {
          break
        }
      }

      barsWithinLineMap[prevPoint] = lineBars
    }

    return barsWithinLineMap
  }

  fun getLineParameters(point1: Point, point2: Point): Pair<Float, Float> {
    val x1 = point1.relativeTime.toFloat()
    val x2 = point2.relativeTime.toFloat()

    val y1 = point1.intensity
    val y2 = point2.intensity

    val a = (y2 - y1) / (x2 - x1)
    val b = y1 - a * x1

    return (a to b)
  }

  // intersection between y = ax + b and y = x (point)
  fun getBarIntersectionPoints(a: Float, b: Float, bar: Bar): Triple<Point?, Point?, Point?>? {
    val (barPoint1, barPoint2) = getBarPoints(bar)

    val intersectionPoint1 = getVerticalIntervalIntersectionPoint(a, b, barPoint1)
    val intersectionPoint2 = getVerticalIntervalIntersectionPoint(a, b, barPoint2)
    val horizontalIntersection = getHorizontalIntersection(a, b, bar)

    return Triple(
      intersectionPoint1,
      if (
        horizontalIntersection != intersectionPoint1 && horizontalIntersection != intersectionPoint2
      )
        horizontalIntersection
      else null,
      intersectionPoint2,
    )
  }

  fun getVerticalIntervalIntersectionPoint(a: Float, b: Float, point: Point): Point? {
    val x = point.relativeTime.toFloat()
    val y = a * x + b

    return if (y < 0 || y > point.intensity) null else Point(y, point.sharpness, point.relativeTime)
  }

  fun getHorizontalIntersection(a: Float, b: Float, bar: Bar): Point? {
    if (a == 0f) {
      return null
    } else {
      val y = bar.intensity
      val x = (y - b) / a

      return if (x < bar.x1 || x > bar.x2) null else Point(bar.intensity, bar.sharpness, x.roundToLong())
    }
  }

  fun getBarPoints(bar: Bar): Pair<Point, Point> {
    val point1 = Point(bar.intensity, bar.sharpness, bar.x1)
    val point2 = Point(bar.intensity, bar.sharpness, bar.x2)

    return (point1 to point2)
  }
}
