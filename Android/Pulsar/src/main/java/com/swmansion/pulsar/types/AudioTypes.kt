package com.swmansion.pulsar.types

data class ValuePoint(
    val time: Float,
    val value: Float
)

/**
 * todo.
 *
 * @param time relative time.
 * @param amplitude value from range [0-1].
 * @param frequency value from range [0-1]. Ignored for devices that do not support envelopes.
 */
data class ConfigPoint(
    val time: Float,
    val amplitude: Float,
    val frequency: Float
)

data class ContinuesPattern(
    val amplitude: List<ValuePoint>,
    val frequency: List<ValuePoint>
)

data class PatternData(
    val continuesPattern: ContinuesPattern,
    val discretePattern: List<ConfigPoint>
) {
    constructor(rawContinuesPattern: List<List<List<Double>>>, rawDiscretePattern: List<List<Double>>) : this(
        continuesPattern = ContinuesPattern(
            amplitude = rawContinuesPattern[0].map { ValuePoint(time = it[0].toFloat(), value = it[1].toFloat()) },
            frequency = rawContinuesPattern[1].map { ValuePoint(time = it[0].toFloat(), value = it[1].toFloat()) }
        ),
        discretePattern = rawDiscretePattern.map { ConfigPoint(time = it[0].toFloat(), amplitude = it[1].toFloat(), frequency = it[2].toFloat()) }
    )
}

enum class WaveformType(val value: String) {
    SINE("sine"),
    SQUARE("square"),
    TRIANGLE("triangle"),
    SAWTOOTH("sawtooth")
}

data class OscillatorConfig(
    val frequency: FrequencyConfig,
    val envelope: EnvelopeConfig,
    val waveform: WaveformType
)

data class FrequencyConfig(
    val initial: Double,
    val final: Double,
    val decayTime: Double
)

data class EnvelopeConfig(
    val attack: Double,
    val decay: Double,
    val sustainLevel: Double,
    val sustainDuration: Double,
    val release: Double
)

data class DiscreteAudioConfig(
    val oscillator: OscillatorConfig,
    val timestamp: Double,
    val volume: Float
)

data class ContinuousAudioConfig(
    val type: String,
    val data: AudioDataConfig
)

data class AudioDataConfig(
    val amplitude: List<ValuePoint>,
    val frequency: List<ValuePoint>
)

data class AudioPatternConfig(
    val discreteData: List<DiscreteAudioConfig>,
    val continuousData: List<ContinuousAudioConfig>
)
