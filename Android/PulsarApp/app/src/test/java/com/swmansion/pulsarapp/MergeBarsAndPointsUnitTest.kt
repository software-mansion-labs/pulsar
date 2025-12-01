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
  fun simpleAscendingTest() {
    val bars =
      arrayListOf(
        Bar(0, 100, 1f, 1f), // beginning
        Bar(200, 300, 1f, 1f), // middle
        Bar(400, 500, 1f, 1f), // end
      )

    val points = arrayListOf(
      Point(0f, 1f, 0),
      Point(1f, 1f, 500),
      Point(0f, 1f, 500)
    )

    val expectedResults =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(1f, 1f, 0),
        Point(1f, 1f, 100),
        Point(0.2f, 1f, 100),
        Point(0.4f, 1f, 200),
        Point(1f, 1f, 200),
        Point(1f, 1f, 300),
        Point(0.6f, 1f, 300),
        Point(0.8f, 1f, 400),
        Point(1f, 1f, 400),
        Point(1f, 1f, 500),
        Point(0f, 1f, 500),
      )

    val mergedPoints = mergePointsAndBars(bars, points)

    assertEquals(expectedResults.size, mergedPoints.size)
    assertEquals(expectedResults, mergedPoints)
  }

  @Test
  fun simpleDescendingTest() {
    val bars =
      arrayListOf(
        Bar(0, 100, 1f, 1f), // beginning
        Bar(200, 300, 1f, 1f), // middle
        Bar(400, 500, 1f, 1f), // end
      )

    val points = arrayListOf(Point(0f, 1f, 0), Point(1f, 1f, 0), Point(0f, 1f, 500))

    val expectedResults =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(1f, 1f, 0),
        Point(1f, 1f, 100),
        Point(0.8f, 1f, 100),
        Point(0.6f, 1f, 200),
        Point(1f, 1f, 200),
        Point(1f, 1f, 300),
        Point(0.4f, 1f, 300),
        Point(0.2f, 1f, 400),
        Point(1f, 1f, 400),
        Point(1f, 1f, 500),
        Point(0f, 1f, 500),
      )

    val mergedPoints = mergePointsAndBars(bars, points)

    assertEquals(expectedResults.size, mergedPoints.size)
    assertEquals(expectedResults, mergedPoints)
  }

  @Test
  fun simpleConstantTest() {
    val bars =
      arrayListOf(
        Bar(0, 100, 1f, 1f), // beginning
        Bar(200, 300, 1f, 1f), // middle
        Bar(400, 500, 1f, 1f), // end
      )

    val points =
      arrayListOf(Point(0f, 1f, 0), Point(0.2f, 1f, 0), Point(0.2f, 1f, 500), Point(0f, 1f, 500))

    val expectedResults =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(1f, 1f, 0),
        Point(1f, 1f, 100),
        Point(0.2f, 1f, 100),
        Point(0.2f, 1f, 200),
        Point(1f, 1f, 200),
        Point(1f, 1f, 300),
        Point(0.2f, 1f, 300),
        Point(0.2f, 1f, 400),
        Point(1f, 1f, 400),
        Point(1f, 1f, 500),
        Point(0f, 1f, 500),
      )

    val mergedPoints = mergePointsAndBars(bars, points)

    assertEquals(expectedResults.size, mergedPoints.size)
    assertEquals(expectedResults, mergedPoints)
  }

  @Test
  fun cutBarsTest() {
    val bars =
      arrayListOf(
        Bar(100, 300, 0.4f, 1f), // cut bar
        Bar(350, 400, 0.8f, 1f), // corner bar
      )

    val points = arrayListOf(
      Point(0f, 1f, 0),
      Point(1f, 1f, 500),
      Point(0f, 1f, 500)
    )

    val expectedResults =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 100),
        Point(0.4f, 1f, 100),
        Point(0.4f, 1f, 200),
        Point(0.7f, 1f, 350),
        Point(0.8f, 1f, 350),
        Point(0.8f, 1f, 400),
        Point(1f, 1f, 500),
        Point(0f, 1f, 500),
      )

    val mergedPoints = mergePointsAndBars(bars, points)

    assertEquals(expectedResults.size, mergedPoints.size)
    assertEquals(expectedResults, mergedPoints)
  }

  @Test
  fun barWithCommonRelativeTimeTest() {
    val bars =
      arrayListOf(
        Bar(50, 100, 1f, 1f),
        Bar(100, 150, 0.8f, 1f),
        Bar(200, 250, 0.8f, 1f),
        Bar(250, 300, 1f, 1f),
        Bar(300, 350, 0.8f, 1f),
      )

    val points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 0),
        Point(0.2f, 1f, 500),
        Point(0f, 1f, 500)
      )

    val expectedResults =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 0),
        Point(0.2f, 1f, 50),
        Point(1f, 1f, 50),
        Point(1f, 1f, 100),
        Point(0.8f, 1f, 100),
        Point(0.8f, 1f, 150),
        Point(0.2f, 1f, 150),
        Point(0.2f, 1f, 200),
        Point(0.8f, 1f, 200),
        Point(0.8f, 1f, 250),
        Point(1f, 1f, 250),
        Point(1f, 1f, 300),
        Point(0.8f, 1f, 300),
        Point(0.8f, 1f, 350),
        Point(0.2f, 1f, 350),
        Point(0.2f, 1f, 500),
        Point(0f, 1f, 500),
      )

    val mergedPoints = mergePointsAndBars(bars, points)

    assertEquals(expectedResults.size, mergedPoints.size)
    assertEquals(expectedResults, mergedPoints)
  }

  @Test
  fun multipleConnectedBarsWithSameAmplitudeTest() {
    val bars =
      arrayListOf(
        Bar(50, 100, 1f, 1f),
        Bar(100, 150, 1f, 1f),
        Bar(200, 250, 1f, 1f),
        Bar(250, 300, 1f, 1f),
        Bar(300, 350, 1f, 1f),
        Bar(350, 400, 1f, 1f),
      )

    val points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 0),
        Point(0.2f, 1f, 500),
        Point(0f, 1f, 500)
      )

    val expectedResults =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 0),
        Point(0.2f, 1f, 50),
        Point(1f, 1f, 50),
        Point(1f, 1f, 150),
        Point(0.2f, 1f, 150),
        Point(0.2f, 1f, 200),
        Point(1f, 1f, 200),
        Point(1f, 1f, 400),
        Point(0.2f, 1f, 400),
        Point(0.2f, 1f, 500),
        Point(0f, 1f, 500),
      )

    val mergedPoints = mergePointsAndBars(bars, points)

    print(mergedPoints)

    assertEquals(expectedResults.size, mergedPoints.size)
    assertEquals(expectedResults, mergedPoints)
  }

  @Test
  fun differentFrequencyEdgeTest() {
    val bars = arrayListOf(Bar(400, 500, 1f, 1f), Bar(500, 600, 0.8f, 1f))

    val points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 0),
        Point(0.2f, 1f, 500),
        Point(1f, 1f, 1000),
        Point(0f, 1f, 1000),
      )

    val expectedResults =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 0),
        Point(0.2f, 1f, 400),
        Point(1f, 1f, 400),
        Point(1f, 1f, 500),
        Point(0.8f, 1f, 500),
        Point(0.8f, 1f, 600),
        Point(0.36f, 1f, 600),
        Point(1f, 1f, 1000),
        Point(0f, 1f, 1000),
      )

    val mergedPoints = mergePointsAndBars(bars, points)

    assertEquals(expectedResults.size, mergedPoints.size)
    assertEquals(expectedResults, mergedPoints)
  }

  @Test
  fun sameFrequencyEdgeTest() {
    val bars = arrayListOf(
      Bar(400, 500, 1f, 1f),
      Bar(500, 600, 1f, 1f))

    val points =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 0),
        Point(0.2f, 1f, 500),
        Point(1f, 1f, 1000),
        Point(0f, 1f, 1000),
      )

    val expectedResults =
      arrayListOf(
        Point(0f, 1f, 0),
        Point(0.2f, 1f, 0),
        Point(0.2f, 1f, 400),
        Point(1f, 1f, 400),
        Point(1f, 1f, 600),
        Point(0.36f, 1f, 600),
        Point(1f, 1f, 1000),
        Point(0f, 1f, 1000),
      )

    val mergedPoints = mergePointsAndBars(bars, points)

    assertEquals(expectedResults.size, mergedPoints.size)
    assertEquals(expectedResults, mergedPoints)
  }
}
