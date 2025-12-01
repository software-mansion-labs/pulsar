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
class ConvertBarsToPointsUnitTest {
  @Test
  fun simpleTest() {
    val bars =
      arrayListOf(
        Bar(100, 200, 0.3f, 0.8f),
        Bar(300, 500, 0.6f, 0.8f),
        Bar(600, 800, 1f, 0.8f)
      )

    val points = convertBarsToPoints(bars)

    val expectedPoints: ArrayList<Point> =
      arrayListOf(
        Point(0f, 0f, 0),
        Point(0f, 0.8f, 100),
        Point(0.3f, 0.8f, 100),
        Point(0.3f, 0.8f, 200),
        Point(0f, 0.8f, 200),
        Point(0f, 0.8f, 300),
        Point(0.6f, 0.8f, 300),
        Point(0.6f, 0.8f, 500),
        Point(0f, 0.8f, 500),
        Point(0f, 0.8f, 600),
        Point(1f, 0.8f, 600),
        Point(1f, 0.8f, 800),
        Point(0f, 0.8f, 800),
      )

    assertEquals(points, expectedPoints)
  }

  @Test
  fun commonPointTest() {
    val bars = arrayListOf(
      Bar(100, 200, 0.3f, 0.8f),
      Bar(200, 300, 0.6f, 0.8f)
    )

    val points = convertBarsToPoints(bars)

    val expectedPoints: ArrayList<Point> =
      arrayListOf(
        Point(0f, 0f, 0),
        Point(0f, 0.8f, 100),
        Point(0.3f, 0.8f, 100),
        Point(0.3f, 0.8f, 200),
        Point(0.6f, 0.8f, 200),
        Point(0.6f, 0.8f, 300),
        Point(0f, 0.8f, 300),
      )

    assertEquals(points, expectedPoints)
  }

  @Test
  fun startWith0Test() {
    val bars = arrayListOf(
      Bar(0, 200, 0.3f, 0.8f)
    )

    val points = convertBarsToPoints(bars)

    val expectedPoints: ArrayList<Point> =
      arrayListOf(
        Point(0f, 0.8f, 0),
        Point(0.3f, 0.8f, 0),
        Point(0.3f, 0.8f, 200),
        Point(0f, 0.8f, 200),
      )

    assertEquals(points, expectedPoints)
  }
}
