package com.swmansion.pulsar.presets

import android.os.Build
import android.os.VibrationEffect
import android.util.Log
import com.swmansion.pulsar.Pulsar
import com.swmansion.pulsar.haptics.HapticEngineWrapper
import com.swmansion.pulsar.types.PatternData
import com.swmansion.pulsar.types.Preset
import com.swmansion.pulsar.types.PresetWithName

class SystemPrimitivePresets(private val engine: HapticEngineWrapper) {
    fun primitiveClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            playHaptic(VibrationEffect.Composition.PRIMITIVE_CLICK)
        }
    }
    fun primitiveThud() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            playHaptic(VibrationEffect.Composition.PRIMITIVE_THUD)
        }
    }
    fun primitiveSpin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            playHaptic(VibrationEffect.Composition.PRIMITIVE_SPIN)
        }
    }
    fun primitiveQuickRise() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            playHaptic(VibrationEffect.Composition.PRIMITIVE_QUICK_RISE)
        }
    }
    fun primitiveSlowRise() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            playHaptic(VibrationEffect.Composition.PRIMITIVE_SLOW_RISE)
        }
    }
    fun primitiveQuickFall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            playHaptic(VibrationEffect.Composition.PRIMITIVE_QUICK_FALL)
        }
    }
    fun primitiveTick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            playHaptic(VibrationEffect.Composition.PRIMITIVE_TICK)
        }
    }
    fun primitiveLowTick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            playHaptic(VibrationEffect.Composition.PRIMITIVE_LOW_TICK)
        }
    }

    private fun playHaptic(primitive: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            engine.vibrate(
                VibrationEffect.startComposition()
                    .addPrimitive(primitive)
                    .compose()
            )
        } else {
            Log.w("Pulsar", "Incompatible Android version. System primitive preset unsupported")
        }
    }
}

class SystemPrimitiveClickPreset(haptics: Pulsar, private val systemPresets: SystemPrimitivePresets) : Preset,
    Player(
        haptics,
//CODEGEN_BEGIN_{system_preset}
        PatternData(
            rawDiscretePattern = listOf(
                listOf(0.0f, 0.65f, 0.85f),
            ),
        ),
//CODEGEN_END_{system_preset}
        true
    ) {
    override fun play() {
        super.play()
        systemPresets.primitiveClick()
    }
    companion object: PresetWithName { override val name = "SystemPrimitiveClickPreset" }
}

class SystemPrimitiveThudPreset(haptics: Pulsar, private val systemPresets: SystemPrimitivePresets) : Preset,
    Player(
        haptics,
//CODEGEN_BEGIN_{system_preset}
        PatternData(
            rawDiscretePattern = listOf(
                listOf(0.0f, 0.9f, 0.2f),
            ),
        ),
//CODEGEN_END_{system_preset}
        true
    ) {
    override fun play() {
        super.play()
        systemPresets.primitiveThud()
    }
    companion object: PresetWithName { override val name = "SystemPrimitiveThudPreset" }
}

class SystemPrimitiveSpinPreset(haptics: Pulsar, private val systemPresets: SystemPrimitivePresets) : Preset,
    Player(
        haptics,
//CODEGEN_BEGIN_{system_preset}
        PatternData(
            rawDiscretePattern = listOf(
                listOf(0.0f, 0.5f, 0.5f),
            ),
        ),
//CODEGEN_END_{system_preset}
        true
    ) {
    override fun play() {
        super.play()
        systemPresets.primitiveSpin()
    }
    companion object: PresetWithName { override val name = "SystemPrimitiveSpinPreset" }
}

class SystemPrimitiveQuickRisePreset(haptics: Pulsar, private val systemPresets: SystemPrimitivePresets) : Preset,
    Player(
        haptics,
//CODEGEN_BEGIN_{system_preset}
        PatternData(
            rawDiscretePattern = listOf(
                listOf(0.0f, 0.7f, 0.6f),
            ),
        ),
//CODEGEN_END_{system_preset}
        true
    ) {
    override fun play() {
        super.play()
        systemPresets.primitiveQuickRise()
    }
    companion object: PresetWithName { override val name = "SystemPrimitiveQuickRisePreset" }
}

class SystemPrimitiveSlowRisePreset(haptics: Pulsar, private val systemPresets: SystemPrimitivePresets) : Preset,
    Player(
        haptics,
//CODEGEN_BEGIN_{system_preset}
        PatternData(
            rawDiscretePattern = listOf(
                listOf(0.0f, 0.5f, 0.4f),
            ),
        ),
//CODEGEN_END_{system_preset}
        true
    ) {
    override fun play() {
        super.play()
        systemPresets.primitiveSlowRise()
    }
    companion object: PresetWithName { override val name = "SystemPrimitiveSlowRisePreset" }
}

class SystemPrimitiveQuickFallPreset(haptics: Pulsar, private val systemPresets: SystemPrimitivePresets) : Preset,
    Player(
        haptics,
//CODEGEN_BEGIN_{system_preset}
        PatternData(
            rawDiscretePattern = listOf(
                listOf(0.0f, 0.7f, 0.7f),
            ),
        ),
//CODEGEN_END_{system_preset}
        true
    ) {
    override fun play() {
        super.play()
        systemPresets.primitiveQuickFall()
    }
    companion object: PresetWithName { override val name = "SystemPrimitiveQuickFallPreset" }
}

class SystemPrimitiveTickPreset(haptics: Pulsar, private val systemPresets: SystemPrimitivePresets) : Preset,
    Player(
        haptics,
//CODEGEN_BEGIN_{system_preset}
        PatternData(
            rawDiscretePattern = listOf(
                listOf(0.0f, 0.2f, 0.9f),
            ),
        ),
//CODEGEN_END_{system_preset}
        true
    ) {
    override fun play() {
        super.play()
        systemPresets.primitiveTick()
    }
    companion object: PresetWithName { override val name = "SystemPrimitiveTickPreset" }
}

class SystemPrimitiveLowTickPreset(haptics: Pulsar, private val systemPresets: SystemPrimitivePresets) : Preset,
    Player(
        haptics,
//CODEGEN_BEGIN_{system_preset}
        PatternData(
            rawDiscretePattern = listOf(
                listOf(0.0f, 0.2f, 0.5f),
            ),
        ),
//CODEGEN_END_{system_preset}
        true
    ) {
    override fun play() {
        super.play()
        systemPresets.primitiveLowTick()
    }
    companion object: PresetWithName { override val name = "SystemPrimitiveLowTickPreset" }
}
