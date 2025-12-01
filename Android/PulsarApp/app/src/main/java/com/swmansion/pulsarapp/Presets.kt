package com.swmansion.pulsarapp

import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.Point
import com.swmansion.pulsarapp.types.Preset
import kotlin.collections.arrayListOf

val SUCCESS_PRESET =
  Preset(
    name = "Success",
    bars =
      arrayListOf(
        Bar(0, 100, 0.809f, 0.616f),
        Bar(200, 300, 0.809f, 0.619f),
        Bar(550, 650, 1f, 1f)
      ),
  )
val FAIL_PRESET =
  Preset(
    name = "Fail",
    bars =
      arrayListOf(
        Bar(0, 100, 0.809f, 0.616f),
        Bar(200, 300, 0.809f, 0.619f),
        Bar(550, 650, 0.591f, 0.309f),
      ),
  )
val ENVELOPE_PRESET =
  Preset(
    name = "Envelope",
    points =
      arrayListOf(
        Point(1.0f, 0.8f, 0),
        Point(0.0f, 0.8f, 500),
        Point(0.0f, 0.8f, 1000),
        Point(1.0f, 0.8f, 2000),
        Point(0.0f, 0.8f, 2000),
      ),
  )
val FALLING_BRICKS =
  Preset(
    name = "Falling Bricks",
    bars =
      arrayListOf(
        Bar(0, 100, 1f, 1f),
        Bar(200, 300, 0.675f, 0.675f),
        Bar(400, 500, 0.406f, 0.2f),
        Bar(600, 700, 0.659f, 0.659f),
        Bar(800, 900, 0.941f, 0.941f),
      ),
  )
val EARTHQUAKE_PRESET =
  Preset(
    name = "Earthquake",
    points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.8f, 1f, 400),
        Point(0f, 1f, 400),
        Point(0f, 1f, 500),
        Point(0.8f, 1f, 700),
        Point(0f, 1f, 700),
      ),
  )
val RANDOM_PRESET =
  Preset(
    name = "Random Preset",
    points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.8f, 1f, 400),
        Point(0f, 1f, 400),
        Point(0f, 1f, 500),
        Point(0.8f, 1f, 700),
        Point(0f, 1f, 700),
        Point(0f, 1f, 900),
        Point(1f, 0.9f, 900),
        Point(1f, 0.9f, 1000),
        Point(0f, 0.9f, 1000),
        Point(0f, 0.9f, 1100),
        Point(1f, 0.9f, 1100),
        Point(1f, 0.9f, 1200),
        Point(0f, 0.9f, 1200),
      ),
  )

val LONG_RISING_PRESET =
  Preset(
    name = "Long Rising",
    points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(1f, 1f, 10000),
        Point(0f, 1f, 10000),
      ),
  )

val UP_PRESET =
  Preset(
    name = "Up",
    points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(1f, 1f, 50), // 50ms
        Point(0f, 1f, 50),
        Point(0f, 1f, 1050),
        Point(1f, 1f, 1200), // 150ms
        Point(0f, 1f, 1200),
        Point(0f, 1f, 2200),
        Point(1f, 1f, 2500), // 300ms
        Point(0f, 1f, 2500),
        Point(0f, 1f, 3500),
        Point(1f, 1f, 4100), // 600ms
        Point(0f, 1f, 4100),
        Point(0f, 1f, 5100),
        Point(1f, 1f, 6100), // 1000ms
        Point(0f, 1f, 6100),
        Point(0f, 1f, 7100),
        Point(1f, 1f, 10100), // 3000ms
        Point(0f, 1f, 10100),
      ),
  )

val UP_AND_DOWN_PRESET =
  Preset(
    name = "Up and Down",
    points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(1f, 1f, 50), // 50ms
        Point(0f, 1f, 100),
        Point(0f, 1f, 1100),
        Point(1f, 1f, 1250), // 150ms
        Point(0f, 1f, 1400),
        Point(0f, 1f, 2400),
        Point(1f, 1f, 2700), // 300ms
        Point(0f, 1f, 3000),
        Point(0f, 1f, 4000),
        Point(1f, 1f, 4600), // 600ms
        Point(0f, 1f, 5200),
        Point(0f, 1f, 6200),
        Point(1f, 1f, 7200), // 1000ms
        Point(0f, 1f, 8200),
        Point(0f, 1f, 9200),
        Point(1f, 1f, 12200), // 3000ms
        Point(0f, 1f, 15200),
      ),
  )

val COMPLEX_PRESET =
  Preset(
    name = "Complex",
    bars =
      arrayListOf(
        Bar(200, 400, 1f, 1f),
        Bar(1200, 1400, 1f, 1f),
        Bar(2200, 2400, 1f, 1f),
        Bar(7200, 7400, 1f, 1f),
        Bar(8200, 8400, 1f, 1f),
        Bar(9200, 9400, 1f, 1f),
      ),
    points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.9f, 1f, 5000),
        Point(0f, 1f, 10000),
      ),
  )

val TEST =
  Preset(
    name = "Test",
    bars =
      arrayListOf(
        Bar(0, 100, 1f, 1f),
        Bar(400, 500, 0.4f, 1f),
        Bar(500, 600, 1f, 1f),
        Bar(600, 700, 0.4f, 1f),
        Bar(900, 1000, 1f, 1f),
        Bar(1000, 1100, 1f, 1f),
        Bar(1500, 1600, 1f, 1f),
        Bar(1900, 2000, 1f, 1f),
        ),
    points = arrayListOf(
      Point(0f,1f,0),
      Point(0.5f, 1f, 1000),
      Point(0f, 1f, 2000),
      )
  )