package com.swmansion.pulsarapp

import android.util.Log
import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.Line
import com.swmansion.pulsarapp.types.Point
import kotlin.collections.forEach
import kotlin.math.abs

const val STEPS_PER_100_MS = 30
const val DEFAULT_SHARPNESS = 1f

fun convertBarsToPoints(bars: ArrayList<Bar>): ArrayList<Point> {
  val points = arrayListOf(Point(0, 0f), Point(bars.last().x2, 0f))
  return mergePointsAndBars(points, bars)
}

fun mergePointsAndBars(points: ArrayList<Point>, allBars: ArrayList<Bar>): ArrayList<Point> {
  val lines = convertPointsToLines(points)

  // filter invalid bars
  val bars = allBars.filter { shouldBarBeMerged(it, lines) }

  if (lines.isEmpty() || bars.isEmpty()) {
    return points
  }

  val startPoint = Point(0, 0f)
  val endPoint = Point(lines.last().point2.relativeTime, 0f)

  // add start point
  var mergedPoints = arrayListOf(startPoint)

  // add points before first bar
  bars.first().let { bar ->
    getLinePointsFromInterval(startPoint, bar.point1, lines)?.let { mergedPoints.addAll(it) }
  }

  // add points within bars range
  val nBars = bars.size
  for (i in 0..nBars - 1) {
    val currBar = bars[i]

    mergedPoints.add(currBar.point1)
    mergedPoints.add(currBar.point2)

    // add lines between two bars
    val nextBar = if (i + 1 < nBars) bars[i + 1] else null
    nextBar?.let { nextBar ->
      if (currBar.x2 != nextBar.x1) {
        getLinePointsFromInterval(currBar.point2, nextBar.point1, lines)?.let {
          mergedPoints.addAll(it)
        }
      }
    }
  }

  // add points after last bar
  bars.last().let { bar ->
    getLinePointsFromInterval(bar.point2, endPoint, lines)?.let { mergedPoints.addAll(it) }
  }

  // add end point
  mergedPoints.add(endPoint)

  // remove duplicating points
  mergedPoints = ArrayList(mergedPoints.distinct())

  // remove redundant points on the same horizontal line (caused by merging adjacent bars)
  deleteRedundantHorizontalLinePoints(mergedPoints)

  return mergedPoints
}

fun shouldBarBeMerged(bar: Bar, lines: ArrayList<Line>): Boolean {
  getLinePointsFromInterval(bar.point1, bar.point2, lines)?.forEach { point ->
    if (point.intensity >= bar.intensity) {
      return false
    }
  } ?: { Log.i(TAG, "This should not happen") }

  return true
}

fun getLinePointsFromInterval(x1: Point, x2: Point, allLines: ArrayList<Line>): ArrayList<Point>? {
  if (x1.relativeTime == x2.relativeTime) {
    return null
  }
  if (x1.relativeTime > x2.relativeTime) {
    Log.w(TAG, "This should not happen.")
    return null
  }

  // ignore vertical lines
  val lines =
    ArrayList(allLines.filter { (point1, point2) -> point1.relativeTime != point2.relativeTime })

  var addingStarted = false
  val intervalLines = ArrayList<Line>()

  for (currLine in lines) {
    val x1LinePoint = currLine.getPointOnLine(x1.relativeTime)
    val x2LinePoint = currLine.getPointOnLine(x2.relativeTime)

    // x1 and x2 are placed on the same line
    if (!addingStarted && x1LinePoint != null && x2LinePoint != null) {
      intervalLines += Line(x1LinePoint, x2LinePoint)
      break
    }

    if (!addingStarted) { // add first line
      if (x1LinePoint != null && x1LinePoint != currLine.point2) {
        intervalLines += Line(x1LinePoint, currLine.point2)
        addingStarted = true
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

  val intervalPoints = ArrayList<Point>()

  intervalLines.forEach { line ->
    intervalPoints.add(line.point1)
    intervalPoints.add(line.point2)
  }

  return ArrayList(intervalPoints.distinct())
}

private fun deleteRedundantHorizontalLinePoints(points: ArrayList<Point>) {
  val indexesToDelete = ArrayList<Int>()
  val nPoints = points.size

  for (index in 1..nPoints - 2) {
    val prevPoint = points[index - 1]
    val currPoint = points[index]
    val nextPoint = points[index + 1]

    if (prevPoint.intensity == currPoint.intensity && currPoint.intensity == nextPoint.intensity) {
      indexesToDelete.add(index)
    }
  }

  indexesToDelete.reversed().forEach { points.removeAt(it) }
}

fun convertPointsToLines(points: ArrayList<Point>): ArrayList<Line> {
  val lines = ArrayList<Line>()

  for (i in 1..points.size - 1) {
    val prevPoint = points[i - 1]
    val currPoint = points[i]

    lines.add(Line(prevPoint, currPoint))
  }
  return lines
}

fun convertPointsToBars(points: ArrayList<Point>): ArrayList<Bar> {
  val lines = convertPointsToLines(points)
  val bars = convertLinesToBars(lines)
  return bars
}

private fun convertLinesToBars(lines: ArrayList<Line>): ArrayList<Bar> {
  val bars = ArrayList<Bar>()

  lines
    .filter { !it.isVertical() }
    .forEach { line ->
      if (line.isHorizontal()) {
        bars +=
          Bar(
            line.point1.relativeTime,
            line.point2.relativeTime,
            line.point1.intensity,
            DEFAULT_SHARPNESS, // sharpness of this bar will never be used
          )
      } else {
        val intensity1 = line.point1.intensity
        val intensity2 = line.point2.intensity

        val intensityDiff = intensity2 - intensity1
        val isLineAscending = intensityDiff > 0
        val lineDuration = line.point2.relativeTime - line.point1.relativeTime

        val nSteps = (lineDuration * STEPS_PER_100_MS) / 100
        val stepDuration = lineDuration / nSteps
        val stepValue = abs(intensityDiff) / nSteps

        for (i in 0..nSteps - 1) { // TODO fix long last interval length ?
          val x1 = line.point1.relativeTime + stepDuration * i
          val x2 = if (i < nSteps - 1) x1 + stepDuration else line.point2.relativeTime
          val intensity =
            if (isLineAscending) line.point1.intensity + stepValue * i
            else line.point1.intensity - stepValue * i

          bars += Bar(x1, x2, intensity, DEFAULT_SHARPNESS)
        }
      }
    }

  return bars
}
