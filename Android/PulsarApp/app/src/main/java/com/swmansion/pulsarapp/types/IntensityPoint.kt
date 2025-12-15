package com.swmansion.pulsarapp.types

/**
 * Represents single point of intensity plot.
 *
 * @param intensity - should be value from [0-1].
 * @param relativeTime - time relative to the beginning of the vibration.
 */
data class IntensityPoint(val relativeTime: Long, var intensity: Float) {
  override fun toString(): String {
    return "(${relativeTime}, ${intensity})"
  }
}
