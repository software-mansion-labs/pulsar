package com.swmansion.pulsar.presets.generated

import com.swmansion.pulsar.Pulsar
import com.swmansion.pulsar.presets.Player
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