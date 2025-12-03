package com.swmansion.pulsarapp

import android.util.Log
import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.ControlPoint
import com.swmansion.pulsarapp.types.Point
import kotlin.collections.forEach

// helper functions for plotting only

fun printBarsToPlot(bars: ArrayList<Bar>) {
  Log.i(TAG, "----------- BARS -----------")

  Log.i(TAG, getPlotHeader())
  bars.forEach { bar ->
    Log.i(TAG, "${bar.x1} ${bar.intensity}")
    Log.i(TAG, "${bar.x2} ${bar.intensity}")
  }
}

fun printPointsToPlot(points: ArrayList<Point>) {
  Log.i(TAG, "----------- POINTS -----------")

  Log.i(TAG, getPlotHeader())
  points.forEach { Log.i(TAG, "${it.relativeTime} ${it.intensity}") }
}

fun printControlPointsToPlot(controlPoints: ArrayList<ControlPoint>) {
  Log.i(TAG, "----------- CONTROL POINTS -----------")

  val points = convertControlPointsToPoints(controlPoints)
  printPointsToPlot(points)
}

private fun convertControlPointsToPoints(controlPoints: ArrayList<ControlPoint>): ArrayList<Point> {
  var relativeTime = 0L
  val points = ArrayList<Point>()
  points += Point(0f, 1f, 0)

  controlPoints.forEach {
    relativeTime += it.duration
    points += Point(it.intensity, it.sharpness, relativeTime)
  }

  return points
}

private fun getPlotHeader(): String {
  return "relative_time intensity"
}
