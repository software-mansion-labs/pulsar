package com.swmansion.pulsarapp.types

/**
 * Represents single vibration bar.
 *
 * @param x1 Bar start.
 * @param x2 Bar end.
 * @param intensity Bar intensity. Value range [0-1].
 * @param sharpness Bar sharpness. Value range (0-1]. Ignored for devices that do not support
 *   envelopes.
 */
data class Bar(val x1: Long, val x2: Long, val intensity: Float, val sharpness: Float) {
  init {
    verifyRelativeTime(x1)
    verifyRelativeTime(x2)
    if (x1 >= x2) {
      throw Exception("x1 must be smaller than x2.")
    }

    verifyIntensity(intensity)
    verifySharpness(sharpness)
  }

  val point1 = IntensityPoint(x1, intensity)
  val point2 = IntensityPoint(x2, intensity)
}
