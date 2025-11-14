package com.swmansion.pulsarapp.types

import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresApi

data class Preset(
    val name: String,
    val bars: ArrayList<Bar>? = null,
    val controlPoints: ArrayList<EnvelopePoint>? = null
){
    @RequiresApi(Build.VERSION_CODES.O)
    val vibrationEffect = createVibrationEffect()
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createVibrationEffect(): VibrationEffect? {
        bars?.let {
            var timings = longArrayOf()
            var amplitudes = intArrayOf()

            val n = bars.size
            for (i in 0..n - 1) {
                val prevBar = if (i == 0) null else bars[i-1]
                val currBar = bars[i]

                if (prevBar != null && prevBar.x2 != currBar.x1){
                    // add pause between bars
                    val pauseDuration = currBar.x1 - prevBar.x2
                    timings += pauseDuration
                    amplitudes += 0
                }

                val currBarDuration = currBar.x2 - currBar.x1
                timings += currBarDuration
                amplitudes += currBar.amplitude
            }

            return VibrationEffect.createWaveform(timings, amplitudes, -1)
        }

        controlPoints?.let{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                val builder = VibrationEffect.BasicEnvelopeBuilder()
                    .setInitialSharpness(0.0f)

                controlPoints.forEach {
                    builder.addControlPoint(it.intensity, it.sharpness, it.duration)
                }
                return builder.build()
            }
        }

        return null
    }
}