package com.swmansion.pulsar.presets

import com.swmansion.pulsar.Pulsar

interface Preset {
    fun play()
}

interface PresetWithName {
    val name: String
}