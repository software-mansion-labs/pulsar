package com.swmansion.pulsarapp

import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.EnvelopePoint
import com.swmansion.pulsarapp.types.Preset
import kotlin.collections.arrayListOf

val SUCCESS_PRESET =
  Preset(
    name = "Success",
    barsList =
      arrayListOf(
        Bar(100, 200, 0.3f, 0.8f),
        Bar(300, 500, 0.6f, 0.8f),
        Bar(600, 800, 1f, 0.8f)),
  )

val FAILURE_PRESET =
  Preset(
    name = "Failure",
    barsList = arrayListOf(
      Bar(0, 200, 0.3f, 1f),
      Bar(300, 500, 0.6f, 1f), 
      Bar(600, 800, 1f, 1f)),
  )

val ENVELOPE_PRESET =
  Preset(
    name = "Envelope",
    controlPointsList =
      arrayListOf(
        EnvelopePoint(1.0f, 0.8f, 0),
        EnvelopePoint(0.0f, 0.8f, 500),
        EnvelopePoint(0.0f, 0.8f, 2000),
        EnvelopePoint(1.0f, 0.8f, 3000),
        EnvelopePoint(0.0f, 0.8f, 5000),
      ),
  )

val FALLING_BRICKS =
  Preset(
    name = "Falling Bricks",
    barsList = arrayListOf(
      Bar(0, 100, 1f, 1f),
      Bar(200, 300, 0.675f, 0.675f),
      Bar(400, 500, 0.406f, 0.2f),
      Bar(600, 700, 0.659f, 0.659f),
      Bar(800, 900, 0.941f, 0.941f)
    )
  )
