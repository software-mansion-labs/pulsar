package com.swmansion.pulsarapp

import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.Point
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MergeBarsAndPointsUnitTest {
  @Test
  fun horizontalTest() {
    val bars =
      arrayListOf(
        Bar(0, 100, 1f, 1f), // beginning
        Bar(200, 300, 1f, 1f), // middle
        Bar(400, 500, 1f, 1f), // end
      )

    val points = arrayListOf(
      Point(0, 0f),
      Point(0, 0.2f),
      Point(500, 0.2f),
      Point(500, 0f)
    )

    val expectedResults =
      arrayListOf(
        Point(0, 0f),
        Point(0, 1f),
        Point(100, 1f),
        Point(100, 0.2f),
        Point(200, 0.2f),
        Point(200, 1f),
        Point(300, 1f),
        Point(300, 0.2f),
        Point(400, 0.2f),
        Point(400, 1f),
        Point(500, 1f),
        Point(500, 0f),
      )

    assertEquals(expectedResults, mergePointsAndBars(points, bars))
  }

  @Test
  fun adjacentBarsTest() {
    val bars =
      arrayListOf(
        Bar(50, 100, 1f, 1f),
        Bar(100, 150, 0.8f, 1f),
        Bar(200, 250, 0.8f, 1f),
        Bar(250, 300, 1f, 1f),
        Bar(300, 350, 0.8f, 1f),
      )

    val points = arrayListOf(
      Point(0, 0f),
      Point(0, 0.2f),
      Point(500, 0.2f),
      Point(500, 0f)
    )

    val expectedResults =
      arrayListOf(
        Point(0, 0f),
        Point(0, 0.2f),
        Point(50, 0.2f),
        Point(50, 1f),
        Point(100, 1f),
        Point(100, 0.8f),
        Point(150, 0.8f),
        Point(150, 0.2f),
        Point(200, 0.2f),
        Point(200, 0.8f),
        Point(250, 0.8f),
        Point(250, 1f),
        Point(300, 1f),
        Point(300, 0.8f),
        Point(350, 0.8f),
        Point(350, 0.2f),
        Point(500, 0.2f),
        Point(500, 0f),
      )

    assertEquals(expectedResults, mergePointsAndBars(points, bars))
  }

  @Test
  fun deleteRedundantLinePointsTest() {
    val bars =
      arrayListOf(
        Bar(50, 100, 1f, 1f),
        Bar(100, 150, 1f, 1f),
        Bar(200, 250, 1f, 1f),
        Bar(250, 300, 1f, 1f),
        Bar(300, 350, 1f, 1f),
        Bar(350, 400, 1f, 1f),
      )

    val points = arrayListOf(
      Point(0, 0f),
      Point(0, 0.2f),
      Point(500, 0.2f),
      Point(500, 0f)
    )

    val expectedResults =
      arrayListOf(
        Point(0, 0f),
        Point(0, 0.2f),
        Point(50, 0.2f),
        Point(50, 1f),
        Point(150, 1f),
        Point(150, 0.2f),
        Point(200, 0.2f),
        Point(200, 1f),
        Point(400, 1f),
        Point(400, 0.2f),
        Point(500, 0.2f),
        Point(500, 0f),
      )

    assertEquals(expectedResults, mergePointsAndBars(points, bars))
  }

  @Test
  fun barOverlappingMultipleLinesTest() {
    val bars =
      arrayListOf(
        Bar(50, 100, 1f, 1f),
        Bar(100, 150, 1f, 1f),
        Bar(200, 250, 1f, 1f),
        Bar(250, 300, 1f, 1f),
        Bar(300, 350, 1f, 1f),
        Bar(350, 400, 1f, 1f),
      )

    val points = arrayListOf(
      Point(0, 0f),
      Point(0, 0.2f),
      Point(500, 0.2f),
      Point(500, 0f)
    )

    val expectedResults =
      arrayListOf(
        Point(0, 0f),
        Point(0, 0.2f),
        Point(50, 0.2f),
        Point(50, 1f),
        Point(150, 1f),
        Point(150, 0.2f),
        Point(200, 0.2f),
        Point(200, 1f),
        Point(400, 1f),
        Point(400, 0.2f),
        Point(500, 0.2f),
        Point(500, 0f),
      )

    assertEquals(expectedResults, mergePointsAndBars(points, bars))
  }

  @Test
  fun complexTest() {
    val points =
      arrayListOf(
        Point(0, 0f),
        Point(100, 0.2f),
        Point(200, 0.2f),
        Point(300, 1f),
        Point(500, 0f),
        Point(700, 0.2f),
        Point(800, 0.5f),
        Point(800, 0.7f),
        Point(900, 0.7f),
        Point(1000, 0.5f),
        Point(1200, 0f),
        Point(1200, 0.5f),
        Point(1200, 0f),
        Point(1300, 0.9f),
        Point(1600, 0.9f),
        Point(1600, 0f),
      )

    val bars =
      arrayListOf(
        Bar(50, 100, 1f, 1f), // end point
        Bar(550, 650, 1f, 1f), // inside
        Bar(750, 800, 1f, 1f), // vertical end
        Bar(800, 950, 0.9f, 1f), // vertical start
        Bar(1100, 1400, 1f, 1f), // overlapping
      )

    val expectedResults =
      arrayListOf(
        Point(0, 0f),
        Point(50, 0.1f),
        Point(50, 1f),
        Point(100, 1f),
        Point(100, 0.2f),
        Point(200, 0.2f),
        Point(300, 1f),
        Point(500, 0f),
        Point(550, 0.05f),
        Point(550, 1f),
        Point(650, 1f),
        Point(650, 0.15f),
        Point(700, 0.2f),
        Point(750, 0.35f),
        Point(750, 1f),
        Point(800, 1f),
        Point(800, 0.9f),
        Point(950, 0.9f),
        Point(950, 0.6f),
        Point(1000, 0.5f),
        Point(1100, 0.25f),
        Point(1100, 1f),
        Point(1400, 1f),
        Point(1400, 0.9f),
        Point(1600, 0.9f),
        Point(1600, 0f),
      )

    assertEquals(expectedResults, mergePointsAndBars(points, bars))
  }
}
