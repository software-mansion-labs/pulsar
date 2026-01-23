package com.swmansion.pulsar

import com.swmansion.pulsar.audio.AudioSimulator
import com.swmansion.pulsar.composers.PatternComposerImpl
import com.swmansion.pulsar.composers.RealtimeComposerImpl
import com.swmansion.pulsar.haptics.HapticEngineWrapper
import com.swmansion.pulsar.presets.PresetsWrapper

class Pulsar {
    private val engine = HapticEngineWrapper()
    private val audioSimulator = AudioSimulator()
    private var presets: PresetsWrapper? = null
    private val realtimeComposer: RealtimeComposerImpl = RealtimeComposerImpl(engine = engine)

    fun Presets(): PresetsWrapper {
        if (presets == null) {
            presets = PresetsWrapper(haptics = this)
        }
        return presets!!
    }

    fun preloadPresets(presetNames: List<String>) {
        val presets = this.Presets()
        for (presetName in presetNames) {
            presets.preloadPresetByName(presetName)
        }
    }

    fun enableSound(state: Boolean) {
        audioSimulator.enableSound(state)
    }

    fun enableCache(state: Boolean) {
        this.Presets().enableCache(state = state)
    }

    fun clearCache() {
        this.Presets().resetCache()
    }

    fun PatternComposer(): PatternComposerImpl {
        return PatternComposerImpl(engine = engine, audioSimulator = audioSimulator)
    }

    fun RealtimeComposer(): RealtimeComposerImpl {
        return realtimeComposer
    }
}