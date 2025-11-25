package com.swmansion.pulsarapp

import android.util.Log
import com.swmansion.pulsarapp.types.EnvelopePoint
import kotlin.collections.forEach

// helper functions for plotting only

fun printPoints(points: ArrayList<EnvelopePoint>) {
  Log.i(TAG, "x1 x2 x3 x4 relative_time intensity")
  points.forEach { Log.i(TAG, "${it.relativeTime} ${it.intensity}") }
}

fun convertControlPointToPoints(controlPoints: ArrayList<EnvelopePoint>): ArrayList<EnvelopePoint> {
  val points = ArrayList<EnvelopePoint>()

  points += EnvelopePoint(0f, 1f, 0)
  var cnt = 0L

  for (x in controlPoints) {
    cnt += x.relativeTime
    points += EnvelopePoint(x.intensity, x.sharpness, cnt)
  }

  return points
}
