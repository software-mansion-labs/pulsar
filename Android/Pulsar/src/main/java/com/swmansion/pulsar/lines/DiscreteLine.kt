package com.swmansion.pulsar.lines

data class DiscreteEvent(
    val timestamp: Double,
    val intensity: Float,
    val sharpness: Float
)

class DiscreteLine {
    private val events = mutableListOf<DiscreteEvent>()

    val getEvents: List<DiscreteEvent>
        get() = events.toList()

    fun addEvent(timestamp: Double, intensity: Float = 1f, sharpness: Float = 1f) {
        events.add(DiscreteEvent(timestamp, intensity, sharpness))
    }

    fun reset() {
        events.clear()
    }
}
