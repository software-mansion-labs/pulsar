package com.swmansion.pulsar.presets

import com.swmansion.pulsar.Pulsar
import com.swmansion.pulsar.audio.PatternData

// Simple Presets

class EarthquakePreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 1.0), listOf(0.5, 0.5)),
                listOf(listOf(0.0, 0.5), listOf(0.5, 0.5))
            ),
            bar = listOf(
                listOf(0.0, 1.0, 0.5),
                listOf(0.1, 0.9, 0.5),
                listOf(0.2, 0.8, 0.5)
            )
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class SuccessPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 1.0), listOf(0.3, 0.0)),
                listOf(listOf(0.0, 0.8), listOf(0.3, 0.8))
            ),
            bar = listOf(listOf(0.0, 1.0, 0.5))
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class FailPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 1.0), listOf(0.5, 1.0)),
                listOf(listOf(0.0, 0.3), listOf(0.5, 0.3))
            ),
            bar = listOf(
                listOf(0.0, 1.0, 0.3),
                listOf(0.15, 1.0, 0.3),
                listOf(0.3, 1.0, 0.3)
            )
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class TapPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 1.0)),
                listOf(listOf(0.0, 0.5))
            ),
            bar = listOf(listOf(0.0, 1.0, 0.5))
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

// System Impact Presets

class SystemImpactLightPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 0.3), listOf(0.1, 0.0)),
                listOf(listOf(0.0, 0.5), listOf(0.1, 0.5))
            ),
            bar = listOf(listOf(0.0, 0.3, 0.5))
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class SystemImpactMediumPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 0.6), listOf(0.15, 0.0)),
                listOf(listOf(0.0, 0.5), listOf(0.15, 0.5))
            ),
            bar = listOf(listOf(0.0, 0.6, 0.5))
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class SystemImpactHeavyPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 1.0), listOf(0.2, 0.0)),
                listOf(listOf(0.0, 0.5), listOf(0.2, 0.5))
            ),
            bar = listOf(listOf(0.0, 1.0, 0.5))
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class SystemImpactSoftPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 0.2), listOf(0.08, 0.0)),
                listOf(listOf(0.0, 0.5), listOf(0.08, 0.5))
            ),
            bar = listOf(listOf(0.0, 0.2, 0.5))
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class SystemImpactRigidPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 1.0), listOf(0.12, 0.0)),
                listOf(listOf(0.0, 0.8), listOf(0.12, 0.8))
            ),
            bar = listOf(listOf(0.0, 1.0, 0.8))
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

// System Notification Presets

class SystemNotificationSuccessPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 0.5), listOf(0.1, 0.8), listOf(0.2, 0.0)),
                listOf(listOf(0.0, 0.6), listOf(0.1, 0.8), listOf(0.2, 0.8))
            ),
            bar = listOf(
                listOf(0.0, 0.5, 0.6),
                listOf(0.1, 0.8, 0.8)
            )
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class SystemNotificationWarningPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 1.0), listOf(0.1, 1.0), listOf(0.2, 0.0)),
                listOf(listOf(0.0, 0.5), listOf(0.1, 0.5), listOf(0.2, 0.5))
            ),
            bar = listOf(
                listOf(0.0, 1.0, 0.5),
                listOf(0.1, 1.0, 0.5)
            )
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class SystemNotificationErrorPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 1.0), listOf(0.08, 0.5), listOf(0.16, 1.0), listOf(0.24, 0.0)),
                listOf(listOf(0.0, 0.3), listOf(0.08, 0.3), listOf(0.16, 0.3), listOf(0.24, 0.3))
            ),
            bar = listOf(
                listOf(0.0, 1.0, 0.3),
                listOf(0.08, 0.5, 0.3),
                listOf(0.16, 1.0, 0.3)
            )
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}

class SystemSelectionPreset(private val haptics: Pulsar) : Preset {
    override fun play() {
        val hapticData = PatternData(
            line = listOf(
                listOf(listOf(0.0, 0.5), listOf(0.05, 0.0)),
                listOf(listOf(0.0, 0.5), listOf(0.05, 0.5))
            ),
            bar = listOf(listOf(0.0, 0.5, 0.5))
        )
        haptics.PatternComposer().playPattern(hapticData)
    }
}
