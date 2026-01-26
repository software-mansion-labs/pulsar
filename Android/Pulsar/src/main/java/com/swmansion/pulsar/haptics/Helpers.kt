package com.swmansion.pulsar.haptics

import android.os.Build
import android.os.Vibrator
import android.util.Log
import com.swmansion.pulsar.audio.DiscretePoint
import com.swmansion.pulsar.audio.PatternPoint
import com.swmansion.pulsar.types.Bar
import com.swmansion.pulsar.types.Line
import com.swmansion.pulsar.types.Plot
import com.swmansion.pulsar.types.PlotPoint
import kotlin.collections.forEach
import kotlin.math.abs
import kotlin.math.max

const val STEPS_PER_100_MS = 30
const val DEFAULT_SHARPNESS = 1f
const val ENVELOPE_SUPPORT_BAR_DURATION_RANGE_MS = 30
const val DEFAULT_BAR_DURATION_RANGE_MS = 70
const val DEFAULT_MIN_BAR_DURATION_MS = 30L

fun convertIntensityToLines(intensity: List<PatternPoint>): ArrayList<Line> {
  val lines = ArrayList<Line>()

  for (i in 1..< intensity.size) {
    val prevPoint = intensity[i - 1]
    val currPoint = intensity[i]

    lines.add(Line(prevPoint, currPoint))
  }
  return lines
}

// TODO: sharpness of these bars will never be used - this function should be used only on devices
// that do not support envelopes, because they do not use sharpness
fun generateBarsFromPlot(plot: Plot): ArrayList<Bar> {
  val lines = convertIntensityToLines(plot.intensity)
  val bars = ArrayList<Bar>()

  lines
    .filter { !it.isVertical() }
    .forEach { line ->
      if (line.isHorizontal()) {
        bars +=
          Bar(
            line.point1.time.toLong(),
            line.point2.time.toLong(),
            line.point1.value,
            DEFAULT_SHARPNESS,
          )
      } else { // approximate line with bars
        val intensity1 = line.point1.value
        val intensity2 = line.point2.value

        val intensityDiff = intensity2 - intensity1
        val isLineAscending = intensityDiff > 0
        val lineDuration = line.point2.time - line.point1.time

        val nSteps = max((lineDuration.toLong() * STEPS_PER_100_MS) / 100, 1)
        val stepDuration = lineDuration / nSteps
        val stepValue = abs(intensityDiff) / nSteps

        // TODO: last step sometimes is longer than stepDuration, because we use int step value
        // TODO: middle of the step can be used instead of the beginning
        for (i in 0..<nSteps) {
          val x1 = line.point1.time + stepDuration * i
          val x2 = if (i < nSteps - 1) x1 + stepDuration else line.point2.time
          val intensity =
            if (isLineAscending) line.point1.value + stepValue * i
            else line.point1.value - stepValue * i

          bars += Bar(x1.toLong(), x2.toLong(), intensity, DEFAULT_SHARPNESS)
        }
      }
    }

  return bars
}

fun generateComplexPlot(plot: Plot, initBars: List<Bar>): Plot {
  val (intensity, sharpness) = plot
  val lines = convertIntensityToLines(intensity)

  val startPoint = intensity.first()
  val endPoint = intensity.last()

  // fit bars to plot
  val bars =
    initBars
      .filter { // delete bars outside plot
        if (it.x1 < endPoint.time) true
        else {
          Log.w(TAG, "Bar $it will be omitted, because it's not within plot range.")
          false
        }
      }
      .map { // cut bars to fit the plot
        if (it.x2 <= endPoint.time) it
        else {
          Log.w(TAG, "Bar $it will be cut, because it do not fit plot range.")
          Bar(it.x1, endPoint.time.toLong(), it.intensity, it.sharpness)
        }
      }
      .filter { shouldBarBeMerged(it, lines) } // use only bars above plot

  if (bars.isEmpty()) {
    return plot
  }

  // create complex plot

  var complexIntensity = arrayListOf(startPoint)
  val complexSharpness = arrayListOf<PatternPoint>()

  val nBars = bars.size
  for (i in 0..< nBars) {
    val currBar = bars[i]
    val nextBar = if (i + 1 < nBars) bars[i + 1] else null

    // handle interval before first bar
    if (i == 0) {
      addComplexPointsFromInterval(
        x1 = startPoint.time.toLong(),
        x2 = currBar.x1,
        lines = lines,
        sharpness = sharpness,
        complexIntensity = complexIntensity,
        complexSharpness = complexSharpness,
      )
    }

    // add bar intensity
    complexIntensity.add(currBar.point1)
    complexIntensity.add(currBar.point2)

    // add bar sharpness
    complexSharpness.add(PatternPoint(currBar.x1.toFloat(), currBar.sharpness))

    // handle interval between currBar and nextBar
    nextBar?.let { nextBar ->
      addComplexPointsFromInterval(
        x1 = currBar.x2,
        x2 = nextBar.x1,
        lines = lines,
        sharpness = sharpness,
        complexIntensity = complexIntensity,
        complexSharpness = complexSharpness,
      )
    }

    // handle interval after last bar
    if (i == nBars - 1) {
      addComplexPointsFromInterval(
        x1 = currBar.x2,
        x2 = endPoint.time.toLong(),
        lines = lines,
        sharpness = sharpness,
        complexIntensity = complexIntensity,
        complexSharpness = complexSharpness,
      )
    }
  }

  complexIntensity.add(endPoint)

  // remove duplicates
  complexIntensity = ArrayList(complexIntensity.distinct())
  // remove redundant points on horizontal lines caused by merging adjacent bars
  deleteRedundantHorizontalLinePoints(complexIntensity)

  // remove redundant changes caused by adding bars to plot
  deleteRedundantSharpness(complexSharpness)

  return Plot(complexIntensity, complexSharpness)
}

fun shouldBarBeMerged(bar: Bar, lines: ArrayList<Line>): Boolean {
  getIntensityFromInterval(bar.x1, bar.x2, lines)?.forEach { point ->
    if (point.value >= bar.intensity) {
      return false
    }
  } ?: { Log.w(TAG, "This should not happen") }

  return true
}

private fun addComplexPointsFromInterval(
  x1: Long,
  x2: Long,
  lines: List<Line>,
  sharpness: List<PatternPoint>,
  complexIntensity: List<PatternPoint>,
  complexSharpness: List<PatternPoint>,
) {
  val intensityPoints = getIntensityFromInterval(x1, x2, lines)
  val sharpnessPoints = getSharpnessFromInterval(x1, x2, sharpness)

  intensityPoints?.let { (complexIntensity as MutableList).addAll(it) }
  sharpnessPoints?.let { (complexSharpness as MutableList).addAll(it) }
}

fun getIntensityFromInterval(
  x1: Long,
  x2: Long,
  allLines: List<Line>,
): List<PatternPoint>? {
  if (x1 == x2) {
    return null
  } else if (x1 > x2) {
    Log.w(TAG, "This should not happen.")
    return null
  } else {
    // ignore vertical lines
    val lines = allLines.filter { !it.isVertical() }

    var foundFirstLine = false
    val intervalLines = ArrayList<Line>()

    for (currLine in lines) {
      val x1LinePoint = currLine.getPoint(x1)
      val x2LinePoint = currLine.getPoint(x2)

      // x1 and x2 are placed on the same line
      if (!foundFirstLine && x1LinePoint != null && x2LinePoint != null) {
        intervalLines += Line(x1LinePoint, x2LinePoint)
        break
      }

      if (!foundFirstLine) { // add first line
        if (x1LinePoint != null && x1LinePoint != currLine.point2) {
          intervalLines += Line(x1LinePoint, currLine.point2)
          foundFirstLine = true
        }
      } else {
        if (x2LinePoint != null) { // add last line
          intervalLines += Line(currLine.point1, x2LinePoint)
          break
        } else { // add lines between
          intervalLines += currLine
        }
      }
    }

    return convertLinesToIntensity(intervalLines)
  }
}

fun getSharpnessFromInterval(
  x1: Long,
  x2: Long,
  sharpness: List<PatternPoint>,
): List<PatternPoint>? {
  if (x1 == x2) {
    return null
  } else if (x1 > x2) {
    Log.w(TAG, "This should not happen.")
    return null
  } else {

    // we need to include < sharpness - in case last sharpness change was within bar
    val startSharpness = sharpness.findLast { it.time <= x1 }?.value
    val intervalSharpness = sharpness.filter { x1 < it.time && it.time < x2 }

    val resultSharpness = ArrayList<PatternPoint>()

    startSharpness?.let {
      val point = PatternPoint(x1.toFloat(), it)
      resultSharpness.add(point)
    } ?: { Log.w(TAG, "This should not happen.") }

    resultSharpness.addAll(intervalSharpness)

    return resultSharpness
  }
}

private fun deleteRedundantHorizontalLinePoints(points: List<PatternPoint>) {
  val indexesToDelete = ArrayList<Int>()
  val nPoints = points.size

  for (index in 1..nPoints - 2) {
    val prevPoint = points[index - 1]
    val currPoint = points[index]
    val nextPoint = points[index + 1]

    if (prevPoint.value == currPoint.value && currPoint.value == nextPoint.value) {
      indexesToDelete.add(index)
    }
  }

  indexesToDelete.reversed().forEach { (points as MutableList).removeAt(it) }
}

private fun deleteRedundantSharpness(sharpness: List<PatternPoint>) {
  val indexesToDelete = ArrayList<Int>()
  val nPoints = sharpness.size

  for (index in 1..< nPoints) {
    val prevPoint = sharpness[index - 1]
    val currPoint = sharpness[index]

    if (prevPoint.value == currPoint.value) {
      indexesToDelete.add(index)
    }
  }

  indexesToDelete.reversed().forEach { (sharpness as MutableList).removeAt(it) }
}

fun generatePlotFromBars(bars: List<Bar>): Plot {
  // create simple plot
  val plotIntensity = arrayListOf(PatternPoint(0f, 0f), PatternPoint(bars.last().x2.toFloat(), 0f))
  val plotSharpness =
    arrayListOf(PatternPoint(0f, bars.firstOrNull()?.sharpness ?: DEFAULT_SHARPNESS))

  val plot = Plot(plotIntensity, plotSharpness)

  return generateComplexPlot(plot, bars)
}

fun generatePlotPoints(plot: Plot): ArrayList<PlotPoint> {
  val (intensity, sharpness) = plot

  val firstSharpness = sharpness[0].value
  var prevSharpness = firstSharpness

  val plotPoints = ArrayList<PlotPoint>()

  for (i in 0..intensity.size - 1) {
    val currPoint = intensity[i]
    val nextPoint = if (i + 1 < intensity.size) intensity[i + 1] else null

    // add currPoint
    val currPlotPoint =
      PlotPoint(
        currPoint.time.toLong(),
        currPoint.value,
        if (i == 0) firstSharpness else prevSharpness,
      )
    plotPoints.add(currPlotPoint)

    // duplicate point with changed sharpness if needed
    val currPlotSharpnessChange =
      sharpness.find {
        currPoint.time == it.time && i != 0 && it.value != prevSharpness
      } // omit index 0 and do not duplicate point for vertical lines

    currPlotSharpnessChange?.let {
      val newSharpness = currPlotSharpnessChange.value
      val sharpnessChangePlotPoint =
        PlotPoint(currPoint.time.toLong(), currPoint.value, newSharpness)
      plotPoints.add(sharpnessChangePlotPoint)

      prevSharpness = newSharpness
    }

    // add all sharpness change points between currPoint and nextPoint
    nextPoint?.let {
      val line = Line(currPoint, nextPoint)
      val sharpnessOnLine =
        sharpness.filter {
          currPoint.time < it.time && it.time < nextPoint.time
        }

      sharpnessOnLine.forEach {
        val newSharpness = it.value
        val sharpnessChangePoint = line.getPoint(it.time.toLong())

        sharpnessChangePoint?.let { it ->
          plotPoints += PlotPoint(it.time.toLong(), it.value, prevSharpness)
          plotPoints += PlotPoint(it.time.toLong(), it.value, newSharpness)
        } ?: run { Log.w(TAG, "This should not happen.") }

        prevSharpness = newSharpness
      }
    }
  }

  // remove middle plotPoints on vertical lines to archive vertical sharpness transition
  removeVerticalMiddlePlotPoint(plotPoints)

  return plotPoints
}

private fun removeVerticalMiddlePlotPoint(plotPoints: ArrayList<PlotPoint>) {
  val indexesToDelete = ArrayList<Int>()
  val nPoints = plotPoints.size

  for (index in 1..nPoints - 2) {
    val prevPoint = plotPoints[index - 1]
    val currPoint = plotPoints[index]
    val nextPoint = plotPoints[index + 1]

    if (
      prevPoint.relativeTime == currPoint.relativeTime &&
        currPoint.relativeTime == nextPoint.relativeTime
    ) {
      indexesToDelete.add(index)
    }
  }

  indexesToDelete.reversed().forEach { plotPoints.removeAt(it) }
}

fun convertImpulsesToBars(
  vibrationService: Vibrator,
  impulses: List<DiscretePoint>,
): List<Bar> {
  val bars = impulses.map { convertImpulseToBar(vibrationService, it) }
  val resultBars = ArrayList<Bar>()

  var prevBar: Bar? = null
  bars.forEachIndexed { index, currBar ->
    var resultBar: Bar? = null

    if (index == 0) {
      resultBar = currBar
    } else {
      prevBar?.let { prev ->
        if (currBar.x2 > prev.x2) {
          val start = max(prev.x2, currBar.x1) // cut the beginning if there is overlap
          resultBar = Bar(start, currBar.x2, currBar.intensity, currBar.sharpness)
        }
      } ?: run { Log.w(TAG, "This should not happen") }
    }

    resultBar?.let {
      resultBars += it
      prevBar = it
    }
  }

  return resultBars
}

private fun convertImpulseToBar(vibrationService: Vibrator, impulse: DiscretePoint): Bar {
  val isEnvelopeSupported =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA &&
      vibrationService.areEnvelopeEffectsSupported()

  val durationRange =
    if (isEnvelopeSupported) ENVELOPE_SUPPORT_BAR_DURATION_RANGE_MS
    else DEFAULT_BAR_DURATION_RANGE_MS
  val minDuration =
    if (isEnvelopeSupported) vibrationService.envelopeEffectInfo.minControlPointDurationMillis
    else DEFAULT_MIN_BAR_DURATION_MS

  val ratio = 1 - impulse.frequency
  val duration = ratio * durationRange + minDuration
  val r = (duration / 2).toLong()

  return Bar(max(0, impulse.time.toLong() - r), impulse.time.toLong() + r, impulse.amplitude, impulse.frequency)
}

private fun convertLinesToIntensity(lines: List<Line>): List<PatternPoint> {
  val points = ArrayList<PatternPoint>()

  lines.forEach {
    points.add(it.point1)
    points.add(it.point2)
  }

  return ArrayList(points.distinct())
}

fun getBarsWithPauses(bars: List<Bar>): List<Bar> {
  val barsWithPauses = ArrayList<Bar>()
  val n = bars.size

  for (i in 0..n - 1) {
    val currBar = bars[i]
    val nextBar = if (i + 1 < n) bars[i + 1] else null

    // create pause at the beginning
    if (i == 0 && currBar.x1 != 0L) {
      barsWithPauses.add(Bar(0, currBar.x1, 0f, currBar.sharpness))
    }

    barsWithPauses.add(currBar)

    // create pause between bars
    if (nextBar != null && currBar.x2 != nextBar.x1) {
      barsWithPauses.add(Bar(currBar.x2, nextBar.x1, 0f, currBar.sharpness))
    }
  }

  return barsWithPauses
}
