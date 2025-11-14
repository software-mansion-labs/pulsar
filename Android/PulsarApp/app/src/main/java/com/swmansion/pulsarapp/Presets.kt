package com.swmansion.pulsarapp

import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.BarPreset
import kotlin.collections.arrayListOf

val SUCCESS_PRESET = BarPreset(
    name = "Success",
    bars = arrayListOf(
        Bar(0, 300, (0.8 * 255).toInt()),
        Bar(400, 500, (0.6 * 255).toInt()),
        Bar(600, 700, (0.6 * 255).toInt())
    )
)

val FAILURE_PRESET = BarPreset(
    name = "Failure",
    bars = arrayListOf(
        Bar(0, 200, (0.3 * 255).toInt()),
        Bar(300, 500, (0.6 * 255).toInt()),
        Bar(600, 800, 1 * 255)
    )
)