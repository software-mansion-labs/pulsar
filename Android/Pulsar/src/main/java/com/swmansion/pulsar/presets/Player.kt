package com.swmansion.pulsar.presets

import com.swmansion.pulsar.Pulsar
import com.swmansion.pulsar.composers.PatternComposerImpl

class Player(
    private val haptics: Pulsar,
    private val composer: PatternComposerImpl
) {
    fun playPreset(presetName: String) {
        val preset = haptics.Presets().getByName(presetName)
        preset?.play()
    }

    fun stop() {
        composer.stop()
    }
}
