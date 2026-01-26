package com.swmansion.pulsar.types

import com.swmansion.pulsar.audio.DiscretePoint

/**
 * Represents vibration preset. At least one of impulses or plot should be passed. If both of them
 * are passed, the result will be a combination of plot and impulses.
 */
data class Preset(
  val name: String,
  val impulses: ArrayList<DiscretePoint>? = null,
  val plot: Plot? = null,
) {
  init {
    if (impulses == null && plot == null) {
      throwInitException("At least one of impulses or plot must be declared.")
    }

    impulses?.let { verifyImpulses(it) }
  }

  private fun verifyImpulses(impulses: ArrayList<DiscretePoint>) {
    if (impulses.isEmpty())
      throwInitException(
        "Property impulses is invalid. When impulses is passed it must contain at least one impulse."
      )

    impulses.forEachIndexed { index, impulse ->
      if (index > 0) {
        val currImpulse = impulses[index]
        val prevImpulse = impulses[index - 1]

        if (prevImpulse.time == currImpulse.time) {
          throwInitException(
            "Found invalid impulses: $prevImpulse, $currImpulse. Impulses cannot have same relative time."
          )
        }
      }
    }

    val impulseComparator = Comparator { impulse1: DiscretePoint, impulse2: DiscretePoint ->
      compareImpulses(impulse1, impulse2)
    }
    if (impulses != impulses.sortedWith(impulseComparator))
      throwInitException(
        "Property impulses is invalid. Impulses relative time should be in ascending order."
      )
  }

  private fun compareImpulses(impulse1: DiscretePoint, impulse2: DiscretePoint): Int {
    val diff = impulse1.time - impulse2.time
    return diff.toInt()
  }

  private fun throwInitException(message: String) {
    throw Exception("Failed to init ${name.uppercase()}_PRESET. $message")
  }
}
