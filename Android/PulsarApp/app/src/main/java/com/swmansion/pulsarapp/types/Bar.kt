package com.swmansion.pulsarapp.types

/**
 * Represents single vibration bar.
 *
 * @param x1 Bar start.
 * @param x2 Bar end.
 * @param amplitude Bar amplitude. Value range [0-1].
 * @param frequency Bar frequency. Value range (0-1]. Ignored on versions before Android 16
 */
data class Bar(val x1: Long, val x2: Long, val amplitude: Float, val frequency: Float)
