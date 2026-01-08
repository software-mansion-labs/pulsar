package com.swmansion.pulsarapp.types

/**
 * Represents single vibration impulse.
 *
 * @param x Impulse relative time.
 * @param intensity Impulse intensity. Value range [0-1].
 * @param sharpness Impulse sharpness. Value range (0-1]. Ignored for devices that do not support
 *   envelopes.
 */
data class Impulse(val x: Long, val intensity: Float, val sharpness: Float) {
  init {
    verifyRelativeTime(x)
    verifyIntensity(intensity)
    verifySharpness(sharpness)
  }
}
