package com.swmansion.pulsarapp.types

data class Preset(
  val name: String,
  val barsList: ArrayList<Bar>? = null,
  val pointsList: ArrayList<EnvelopePoint>? = null,
) {
  init {
    barsList?.let {
      for (bar in it) {
        checkAmplitude(bar.amplitude)
        checkFrequency(bar.frequency)
      }
    }

    pointsList?.let { points ->
      for (point in points) {
        checkAmplitude(point.intensity)
        checkFrequency(point.sharpness)
      }

      val n = points.size
      if (n > 0) {
        val firstPoint = points[0]
        val lastPoint = points[n - 1]

        if (firstPoint.relativeTime != 0L) {
          throw getInitException(
            "Found invalid controlPointsList. First element relativeTime must be 0."
          )
        } else if (n == 1) {
          throw getInitException(
            "Found invalid controlPointsList. It must contain at least two points."
          )
        } else if (lastPoint.intensity != 0f) { // required in basic envelope
          throw getInitException(
            "Found invalid controlPointsList. Last element intensity must be 0."
          )
        }
      }
    }
  }

  private fun checkAmplitude(amplitude: Float) {
    if (amplitude < 0 || amplitude > 1) {
      throw getInitException(
        "Found invalid amplitude: ${amplitude}. Amplitude must be value from [0,1]."
      )
    }
  }

  private fun checkFrequency(frequency: Float) {
    if (frequency <= 0 || frequency > 1) {
      throw getInitException(
        "Found invalid frequency: $frequency. Frequency must be value from (0,1]."
      ) // TODO correct for bars
    }
  }

  fun getInitException(message: String): Exception {
    return Exception("Failed to init ${name.uppercase()}_PRESET. $message")
  }
}
