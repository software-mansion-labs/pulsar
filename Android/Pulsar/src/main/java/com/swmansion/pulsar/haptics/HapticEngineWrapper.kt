package com.swmansion.pulsar.haptics

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

class HapticEngineWrapper(private val context: Context? = null) {
    private var vibrator: Vibrator? = null
    private var initialized: Boolean = false

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context?.let {
                vibrator = ContextCompat.getSystemService(it, Vibrator::class.java)
                if (vibrator?.hasVibrator() == true) {
                    initialized = true
                }
            }
        } else {
            @Suppress("DEPRECATION")
            vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator?.hasVibrator() == true) {
                initialized = true
            }
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(pattern: LongArray, amplitude: IntArray? = null) {
        if (!initialized || vibrator == null) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = if (amplitude != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibrationEffect.createWaveform(pattern, amplitude, -1)
            } else {
                VibrationEffect.createWaveform(pattern, -1)
            }
            vibrator?.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, -1)
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(duration: Long, amplitude: Int = 128) {
        if (!initialized || vibrator == null) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibrationEffect.createOneShot(duration, amplitude)
            } else {
                VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            }
            vibrator?.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(duration)
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun cancel() {
        vibrator?.cancel()
    }

    fun hasVibrator(): Boolean {
        return initialized && vibrator?.hasVibrator() == true
    }
}
