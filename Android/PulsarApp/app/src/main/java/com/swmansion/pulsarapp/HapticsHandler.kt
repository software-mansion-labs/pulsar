package com.swmansion.pulsarapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.swmansion.pulsarapp.types.BarPreset

const val TAG = "HapticsHandler"

class HapticsHandler(context: Context) {
    private val vibrationService = context.getSystemService(Vibrator::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    fun playPresetVibration(preset: BarPreset){
        vibrationService.vibrate(preset.vibrationEffect)
    }
    fun isAmplitudeSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrationService.hasAmplitudeControl()
    }
    fun isEnvelopeSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA && vibrationService.areEnvelopeEffectsSupported()
    }
}