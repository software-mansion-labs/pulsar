package com.swmansion.pulsar.types

/**
 * Represents single control point of envelope builder.
 *
 * @param intensity should be value from [0-1].
 * @param sharpness should be value from [0-1].
 * @param duration transition time in ms.
 */
data class ControlPoint(val intensity: Float, val sharpness: Float, val duration: Long)

