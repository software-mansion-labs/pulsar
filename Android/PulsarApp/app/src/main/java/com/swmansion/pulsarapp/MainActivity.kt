package com.swmansion.pulsarapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swmansion.pulsarapp.types.Preset
import com.swmansion.pulsarapp.ui.theme.PulsarAppTheme

class MainActivity : ComponentActivity() {
  private var hapticsHandler: HapticsHandler? = null

  @SuppressLint("NewApi")
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    hapticsHandler = HapticsHandler(this)

    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      PulsarAppTheme {
        Column(
          modifier = Modifier.fillMaxWidth().fillMaxHeight(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          DeviceInfo()
          VibrationButton(SUCCESS_PRESET)
          VibrationButton(FAILURE_PRESET)
          VibrationButton(ENVELOPE_PRESET, hapticsHandler?.isEnvelopeSupported())
          VibrationButton(FALLING_BRICKS, hapticsHandler?.isEnvelopeSupported())
        }
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @Composable
  fun VibrationButton(preset: Preset, enabled: Boolean? = true) {
    Button(
      modifier = Modifier.padding(6.dp),
      onClick = { hapticsHandler?.playPresetVibration(preset) },
      enabled = enabled ?: true,
    ) {
      Text(preset.name)
    }
  }

  @Composable
  fun DeviceInfo() {
    Column(modifier = Modifier.padding(48.dp)) {
      Text("Device supports amplitude:   ${hapticsHandler?.isAmplitudeSupported()}")
      Text("Device supports envelope:    ${hapticsHandler?.isEnvelopeSupported()}")
    }
  }
}
