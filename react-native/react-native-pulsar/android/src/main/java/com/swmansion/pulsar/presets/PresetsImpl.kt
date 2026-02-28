package com.swmansion.pulsar.presets

import com.swmansion.pulsar.Pulsar
import com.swmansion.pulsar.types.PatternData
import com.swmansion.pulsar.types.Preset
import com.swmansion.pulsar.types.PresetWithName

class EarthquakePreset(haptics: Pulsar) :
    Preset,
    Player(haptics, PatternData(
        rawContinuousPattern = listOf(
            listOf(listOf(100f, 0.3f), listOf(1500f, 0.3f)),
            listOf(listOf(100f, 0.3f), listOf(1500f, 0.3f)),
        ),
        rawDiscretePattern = listOf(
            listOf(1000f, 1.0f, 1.0f),
        )
    )) {
    companion object: PresetWithName {
        override val name = "Earthquake"
    }
}

class SuccessPreset(haptics: Pulsar) :
    Preset,
    Player(haptics, PatternData(
        rawContinuousPattern = listOf(
            listOf(listOf(100f, 1.0f), listOf(500f, 0.5f), listOf(1600f, 0.0f)),
            listOf(listOf(100f, 0.5f), listOf(500f, 0.5f), listOf(1600f, 0.0f))
        ),
        rawDiscretePattern = listOf()
    )) {
    companion object: PresetWithName {
        override val name = "Success"
    }
}

class FailPreset(private val haptics: Pulsar) :
    Preset,
    Player(haptics, PatternData(
        rawContinuousPattern = listOf(
            listOf(listOf(0f, 1.0f), listOf(500f, 1.0f)),
            listOf(listOf(0f, 0.3f), listOf(500f, 0.3f))
        ),
        rawDiscretePattern = listOf(
            listOf(0f, 1.0f, 0.3f),
            listOf(150f, 1.0f, 0.3f),
            listOf(300f, 1.0f, 0.3f)
        )
    )) {
    companion object: PresetWithName {
        override val name = "Fail"
    }
}

class TapPreset(private val haptics: Pulsar) :
    Preset,
    Player(haptics, PatternData(
        rawContinuousPattern = listOf(
            listOf(listOf(0f, 1.0f)),
            listOf(listOf(0f, 0.5f))
        ),
        rawDiscretePattern = listOf(listOf(0f, 1.0f, 0.5f))
    )) {
    companion object: PresetWithName {
        override val name = "Tap"
    }
}
