package com.swmansion.pulsarapp

import android.util.Log
import com.swmansion.pulsarapp.types.Point
import kotlin.collections.forEach

// helper functions for plotting only

fun printPoints(points: ArrayList<Point>) {
  Log.i(TAG, "x1 x2 x3 x4 relative_time intensity")
  points.forEach { Log.i(TAG, "${it.relativeTime} ${it.intensity}") }
}

fun convertControlPointToPoints(controlPoints: ArrayList<Point>): ArrayList<Point> {
  val points = ArrayList<Point>()

  points += Point(0f, 1f, 0)
  var cnt = 0L

  for (x in controlPoints) {
    cnt += x.relativeTime
    points += Point(x.intensity, x.sharpness, cnt)
  }

  return points
}
