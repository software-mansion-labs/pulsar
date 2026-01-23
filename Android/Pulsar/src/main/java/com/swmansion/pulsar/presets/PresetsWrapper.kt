package com.swmansion.pulsar.presets

import com.swmansion.pulsar.Pulsar

class PresetsWrapper(
    private val haptics: Pulsar
) {
    private var playSound: Boolean = true
    private var useCache: Boolean = true
    private val cache = mutableMapOf<String, Preset>()

    private val mapper: Map<String, (Pulsar) -> Preset?> = mapOf(
        "Earthquake" to { haptics -> EarthquakePreset(haptics) },
        "Success" to { haptics -> SuccessPreset(haptics) },
        "Fail" to { haptics -> FailPreset(haptics) },
        "Tap" to { haptics -> TapPreset(haptics) },
        "SystemImpactLight" to { haptics -> SystemImpactLightPreset(haptics) },
        "SystemImpactMedium" to { haptics -> SystemImpactMediumPreset(haptics) },
        "SystemImpactHeavy" to { haptics -> SystemImpactHeavyPreset(haptics) },
        "SystemImpactSoft" to { haptics -> SystemImpactSoftPreset(haptics) },
        "SystemImpactRigid" to { haptics -> SystemImpactRigidPreset(haptics) },
        "SystemImpactSuccess" to { haptics -> SystemNotificationSuccessPreset(haptics) },
        "SystemNotificationWarning" to { haptics -> SystemNotificationWarningPreset(haptics) },
        "SystemNotificationError" to { haptics -> SystemNotificationErrorPreset(haptics) },
        "SystemSelection" to { haptics -> SystemSelectionPreset(haptics) }
    )

    fun enableCache(state: Boolean) {
        this.useCache = state
        if (!state) {
            resetCache()
        }
    }

    fun isCacheEnabled(): Boolean = this.useCache

    fun resetCache() {
        cache.clear()
    }

    fun preloadPresetByName(name: String) {
        this.useCache = true
        mapper[name]?.let { factory ->
            getCacheablePreset(name, factory)
        }
    }

    fun getByName(name: String): Preset? {
        return if (mapper.containsKey(name)) {
            getCacheablePreset(name, mapper[name]!!)
        } else {
            null
        }
    }

    private fun getCacheablePreset(name: String, factory: (Pulsar) -> Preset?): Preset? {
        return if (useCache) {
            cache.getOrPut(name) {
                factory(haptics) ?: return null
            }
        } else {
            factory(haptics)
        }
    }

    fun Earthquake() {
        mapper["Earthquake"]?.invoke(haptics)?.play()
    }

    fun Success() {
        mapper["Success"]?.invoke(haptics)?.play()
    }

    fun Fail() {
        mapper["Fail"]?.invoke(haptics)?.play()
    }

    fun Tap() {
        mapper["Tap"]?.invoke(haptics)?.play()
    }

    fun SystemImpactLight() {
        mapper["SystemImpactLight"]?.invoke(haptics)?.play()
    }

    fun SystemImpactMedium() {
        mapper["SystemImpactMedium"]?.invoke(haptics)?.play()
    }

    fun SystemImpactHeavy() {
        mapper["SystemImpactHeavy"]?.invoke(haptics)?.play()
    }

    fun SystemImpactSoft() {
        mapper["SystemImpactSoft"]?.invoke(haptics)?.play()
    }

    fun SystemImpactRigid() {
        mapper["SystemImpactRigid"]?.invoke(haptics)?.play()
    }

    fun SystemImpactSuccess() {
        mapper["SystemImpactSuccess"]?.invoke(haptics)?.play()
    }

    fun SystemNotificationWarning() {
        mapper["SystemNotificationWarning"]?.invoke(haptics)?.play()
    }

    fun SystemNotificationError() {
        mapper["SystemNotificationError"]?.invoke(haptics)?.play()
    }

    fun SystemSelection() {
        mapper["SystemSelection"]?.invoke(haptics)?.play()
    }
}
