package com.swmansion.pulsar.haptics

import com.swmansion.pulsar.types.Plot
import com.swmansion.pulsar.types.Preset
import com.swmansion.pulsar.audio.DiscretePoint
import com.swmansion.pulsar.audio.PatternPoint
import kotlin.collections.arrayListOf

val CONST_PLOT_SHARPNESS = arrayListOf(PatternPoint(0f, 1f))
val SUCCESS_PRESET =
  Preset(
    name = "Success",
    impulses =
      arrayListOf(
        DiscretePoint(0f, 0.809f, 0.616f),
        DiscretePoint(150f, 0.809f, 0.619f),
        DiscretePoint(453f, 1f, 1f),
      ),
  )

val FAIL_PRESET =
  Preset(
    name = "Fail",
    impulses =
      arrayListOf(
        DiscretePoint(0f, 0.809f, 0.616f),
        DiscretePoint(150f, 0.809f, 0.619f),
        DiscretePoint(453f, 0.591f, 0.309f),
      ),
  )

val ENVELOPE_PRESET =
  Preset(
    name = "Envelope",
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(0f, 1f),
            PatternPoint(500f, 0f),
            PatternPoint(1000f, 0f),
            PatternPoint(2000f, 1f),
            PatternPoint(2000f, 0f),
          ),
        sharpness = CONST_PLOT_SHARPNESS,
      ),
  )
val FALLING_BRICKS =
  Preset(
    name = "Falling Bricks",
    impulses =
      arrayListOf(
        DiscretePoint(0f, 1f, 1f),
        DiscretePoint(149f, 0.675f, 0.675f),
        DiscretePoint(301f, 0.406f, 0.2f),
        DiscretePoint(501f, 0.659f, 0.659f),
        DiscretePoint(650f, 0.941f, 0.941f),
      ),
  )
val EARTHQUAKE_PRESET =
  Preset(
    name = "Earthquake",
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(300f, 0.8f),
            PatternPoint(300f, 0f),
            PatternPoint(400f, 0f),
            PatternPoint(600f, 0.8f),
            PatternPoint(600f, 0f),
            PatternPoint(1000f, 0f),
          ),
        sharpness = 
          arrayListOf(
            PatternPoint(0f, 0.8f),
            PatternPoint(600f, 0.8f)
          ),
      ),
  )
val RANDOM_PRESET =
  Preset(
    name = "Random",
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(300f, 0.8f),
            PatternPoint(300f, 0f),
            PatternPoint(400f, 0f),
            PatternPoint(600f, 0.8f),
            PatternPoint(600f, 0f),
            PatternPoint(1000f, 0f),
          ),
        sharpness = 
          arrayListOf(
            PatternPoint(0f, 0.8f),
            PatternPoint(600f, 0.8f)
          ),
      ),
    impulses =
      arrayListOf(
        DiscretePoint(834f, 0.834f, 0.3f),
        DiscretePoint(941f, 0.897f, 0.3f),
      ),
  )

val LONG_RISING_PRESET =
  Preset(
    name = "Long Rising",
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(10000f, 1f),
            PatternPoint(10000f, 0f)
          ),
        sharpness = CONST_PLOT_SHARPNESS,
      ),
  )

val UP_PRESET =
  Preset(
    name = "Up",
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(50f, 1f), // 50ms
            PatternPoint(50f, 0f),
            PatternPoint(1050f, 0f),
            PatternPoint(1200f, 1f), // 150ms
            PatternPoint(1200f, 0f),
            PatternPoint(2200f, 0f),
            PatternPoint(2500f, 1f), // 300ms
            PatternPoint(2500f, 0f),
            PatternPoint(3500f, 0f),
            PatternPoint(4100f, 1f), // 600ms
            PatternPoint(4100f, 0f),
            PatternPoint(5100f, 0f),
            PatternPoint(6100f, 1f), // 1000ms
            PatternPoint(6100f, 0f),
            PatternPoint(7100f, 0f),
            PatternPoint(10100f, 1f), // 3000ms
            PatternPoint(10100f, 0f),
          ),
        sharpness = CONST_PLOT_SHARPNESS,
      ),
  )

val UP_AND_DOWN_PRESET =
  Preset(
    name = "Up and Down",
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(50f, 1f), // 50ms
            PatternPoint(100f, 0f),
            PatternPoint(1100f, 0f),
            PatternPoint(1250f, 1f), // 150ms
            PatternPoint(1400f, 0f),
            PatternPoint(2400f, 0f),
            PatternPoint(2700f, 1f), // 300ms
            PatternPoint(3000f, 0f),
            PatternPoint(4000f, 0f),
            PatternPoint(4600f, 1f), // 600ms
            PatternPoint(5200f, 0f),
            PatternPoint(6200f, 0f),
            PatternPoint(7200f, 1f), // 1000ms
            PatternPoint(8200f, 0f),
            PatternPoint(9200f, 0f),
            PatternPoint(12200f, 1f), // 3000ms
            PatternPoint(15200f, 0f),
          ),
        sharpness = CONST_PLOT_SHARPNESS,
      ),
  )

val COMPLEX_PRESET =
  Preset(
    name = "Complex",
    impulses =
      arrayListOf(
        DiscretePoint(200f, 1f, 1f),
        DiscretePoint(1200f, 1f, 1f),
        DiscretePoint(2200f, 1f, 1f),
        DiscretePoint(7200f, 1f, 1f),
        DiscretePoint(8200f, 1f, 1f),
        DiscretePoint(9200f, 1f, 1f),
      ),
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(5000f, 0.9f),
            PatternPoint(10000f, 0f)
          ),
        sharpness = CONST_PLOT_SHARPNESS,
      ),
  )

val TEST_PRESET =
  Preset(
    name = "Test",
    impulses =
      arrayListOf(
        DiscretePoint(0f, 1f, 1f),
        DiscretePoint(400f, 0.4f, 1f),
        DiscretePoint(500f, 1f, 1f),
        DiscretePoint(600f, 0.4f, 1f),
        DiscretePoint(900f, 1f, 1f),
        DiscretePoint(1000f, 1f, 1f),
        DiscretePoint(1500f, 1f, 1f),
        DiscretePoint(1900f, 1f, 1f),
      ),
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(1000f, 0.5f),
            PatternPoint(2000f, 0f)
          ),
        sharpness = CONST_PLOT_SHARPNESS,
      ),
  )

val FREQUENCY_PRESET =
  Preset(
    name = "Frequency",
    impulses =
      arrayListOf(
        DiscretePoint(400f, 1f, 0.1f),
        DiscretePoint(1400f, 0.8f, 0.1f),
        DiscretePoint(2400f, 1f, 0.1f),
        DiscretePoint(3400f, 0.8f, 0.1f),
      ),
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(0f, 0.5f),
            PatternPoint(4000f, 0.5f),
            PatternPoint(4000f, 0f),
          ),
        sharpness =
          arrayListOf(
            PatternPoint(0f, 1f),
            PatternPoint(1000f, 0.75f),
            PatternPoint(2000f, 0.5f),
            PatternPoint(3000f, 0.25f),
          ),
      ),
  )

val MILK_PRESET =
  Preset(
    name = "Milk",
    plot =
      Plot(
        intensity =
          arrayListOf(
            PatternPoint(0f, 0f),
            PatternPoint(651f, 0.813f),
            PatternPoint(651f, 0f),
            PatternPoint(700f, 0f)
          ),
        sharpness =
          arrayListOf(
            PatternPoint(0f, 0.7f),
          ),
      ),
    impulses =
      arrayListOf(
        DiscretePoint(13f, 0.897f, 0.209f),
        DiscretePoint(117f, 0.897f, 0.322f),
        DiscretePoint(253f, 0.903f, 0.484f),
        DiscretePoint(400f, 0.903f, 0.716f),
        DiscretePoint(546f, 0.906f, 0.803f),
        DiscretePoint(651f, 0.906f, 1f),
      ),
  )
