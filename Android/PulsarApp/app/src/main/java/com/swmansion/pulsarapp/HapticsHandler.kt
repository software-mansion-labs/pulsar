package com.swmansion.pulsarapp

import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import com.swmansion.pulsarapp.types.Preset

class HapticsHandler(context: Context) {
    private val vibrationService = context.getSystemService(Vibrator::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    fun playPresetVibration(preset: Preset){
        vibrationService.vibrate(preset.vibrationEffect)
    }
    fun isAmplitudeSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrationService.hasAmplitudeControl()
    }
    fun isEnvelopeSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA && vibrationService.areEnvelopeEffectsSupported()
    }
}