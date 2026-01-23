package com.swmansion.pulsar.lines

class ContinuousLine {
    val intensityCurveLine = IntensityCurveLineModifier()
    val sharpnessCurveLine = SharpnessCurveLineModifier()

    fun reset() {
        intensityCurveLine.reset()
        sharpnessCurveLine.reset()
    }
}
