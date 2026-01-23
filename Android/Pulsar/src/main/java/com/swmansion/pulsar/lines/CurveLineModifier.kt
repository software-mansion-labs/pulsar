package com.swmansion.pulsar.lines

import com.swmansion.pulsar.audio.PatternPoint

interface CurveLineModifier {
    fun addPoint(time: Double, value: Float)
    fun reset()
    fun isEmpty(): Boolean
    fun getDuration(): Double
    fun getCurve(): List<PatternPoint>
}
