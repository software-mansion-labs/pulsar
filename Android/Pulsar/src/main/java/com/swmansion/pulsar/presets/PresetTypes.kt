package com.swmansion.pulsar.presets

import com.swmansion.pulsar.Pulsar

interface Preset {
    fun play()

    companion object {
        fun getInstance(haptics: Pulsar): Preset? = null
    }
}

abstract class BasePreset : Preset {
    abstract val name: String
}
