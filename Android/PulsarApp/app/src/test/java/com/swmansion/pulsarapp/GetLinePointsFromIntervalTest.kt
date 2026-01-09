package com.swmansion.pulsarapp

import com.swmansion.pulsarapp.types.Bar
import com.swmansion.pulsarapp.types.IntensityPoint
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GetLinePointsFromIntervalTest {
  @Test
  fun singleLineTest() {
    val points = arrayListOf(IntensityPoint(0, 0f), IntensityPoint(1000, 1f))

    val start = IntensityPoint(0, 0f)
    val middle1 = IntensityPoint(400, 0f)
    val middle2 = IntensityPoint(800, 0f)
    val end = IntensityPoint(1000, 0f)

    val startLinePoint = IntensityPoint(0, 0f)
    val middle1LinePoint = IntensityPoint(400, 0.4f)
    val middle2LinePoint = IntensityPoint(800, 0.8f)
    val endLinePoint = IntensityPoint(1000, 1f)

    val lines = generateLines(points)

    // start / end
    assertEquals(
      arrayListOf(startLinePoint, middle1LinePoint),
      getIntensityFromInterval(start.relativeTime, middle1.relativeTime, lines),
    )
    assertEquals(
      arrayListOf(middle2LinePoint, endLinePoint),
      getIntensityFromInterval(middle2.relativeTime, end.relativeTime, lines),
    )

    // middle
    assertEquals(
      arrayListOf(middle1LinePoint, middle2LinePoint),
      getIntensityFromInterval(middle1.relativeTime, middle2.relativeTime, lines),
    )
    assertEquals(
      arrayListOf(middle2LinePoint, endLinePoint),
      getIntensityFromInterval(middle2.relativeTime, end.relativeTime, lines),
    )

    // whole
    assertEquals(
      arrayListOf(startLinePoint, endLinePoint),
      getIntensityFromInterval(start.relativeTime, end.relativeTime, lines),
    )
  }

  @Test
  fun multipleLinesFromStartTest() {
    val points =
      arrayListOf(
        IntensityPoint(0, 0f),
        IntensityPoint(200, 0.3f),
        IntensityPoint(400, 0.5f),
        IntensityPoint(600, 0.2f),
        IntensityPoint(800, 0.7f),
        IntensityPoint(1000, 0f),
      )

    val lines = generateLines(points)

    val point1 = IntensityPoint(100, 1f)
    val point2 = IntensityPoint(300, 1f)
    val point3 = IntensityPoint(800, 1f)

    val expectedPoints1 = arrayListOf(points[0], IntensityPoint(100, 0.15f))

    val expectedPoints2 = arrayListOf(points[0], points[1], IntensityPoint(300, 0.4f))

    val expectedPoints3 = arrayListOf(points[0], points[1], points[2], points[3], IntensityPoint(800, 0.7f))

    assertEquals(expectedPoints1, getIntensityFromInterval(points[0].relativeTime, point1.relativeTime, lines))
    assertEquals(expectedPoints2, getIntensityFromInterval(points[0].relativeTime, point2.relativeTime, lines))
    assertEquals(expectedPoints3, getIntensityFromInterval(points[0].relativeTime, point3.relativeTime, lines))
  }

  @Test
  fun multipleLinesFromEndTest() {
    val points =
      arrayListOf(
        IntensityPoint(0, 0f),
        IntensityPoint(200, 0.3f),
        IntensityPoint(400, 0.5f),
        IntensityPoint(600, 0.2f),
        IntensityPoint(800, 0.7f),
        IntensityPoint(1000, 0f),
      )

    val lines = generateLines(points)

    val point1 = IntensityPoint(100, 1f)
    val point2 = IntensityPoint(300, 1f)
    val point3 = IntensityPoint(800, 1f)

    val expectedPoints1 =
      arrayListOf(IntensityPoint(100, 0.15f), points[1], points[2], points[3], points[4], points[5])

    val expectedPoints2 = arrayListOf(IntensityPoint(300, 0.4f), points[2], points[3], points[4], points[5])

    val expectedPoints3 = arrayListOf(points[4], points[5])

    assertEquals(expectedPoints1, getIntensityFromInterval(point1.relativeTime, points[5].relativeTime, lines))
    assertEquals(expectedPoints2, getIntensityFromInterval(point2.relativeTime, points[5].relativeTime, lines))
    assertEquals(expectedPoints3, getIntensityFromInterval(point3.relativeTime, points[5].relativeTime, lines))
  }

  @Test
  fun verticalLineStartEndTest() {
    val points =
      arrayListOf(
        IntensityPoint(0, 0f),
        IntensityPoint(100, 0.5f),
        IntensityPoint(200, 0.5f),
        IntensityPoint(200, 0f),
        IntensityPoint(300, 0f),
        IntensityPoint(300, 0.5f),
        IntensityPoint(400, 0.5f),
        IntensityPoint(400, 0f),
      )

    val lines = generateLines(points)

    val start = IntensityPoint(200L, 1f)
    val middle = IntensityPoint(250L, 1f)
    val end = IntensityPoint(300L, 1f)

    val startLinePoint = points[3]
    val middleLinePoint = IntensityPoint(250, 0f)
    val endLinePoint = points[4]

    assertEquals(
      arrayListOf(startLinePoint, middleLinePoint),
      getIntensityFromInterval(start.relativeTime, middle.relativeTime, lines),
    )
    assertEquals(
      arrayListOf(middleLinePoint, endLinePoint),
      getIntensityFromInterval(middle.relativeTime, end.relativeTime, lines),
    )
    assertEquals(
      arrayListOf(startLinePoint, endLinePoint),
      getIntensityFromInterval(start.relativeTime, end.relativeTime, lines),
    )
  }

  @Test
  fun multipleVerticalAndHorizontalLineTest() {
    val points =
      arrayListOf(
        IntensityPoint(0, 0f),
        IntensityPoint(100, 0f),
        IntensityPoint(100, 0.5f),
        IntensityPoint(200, 0.5f),
        IntensityPoint(200, 1f),
        IntensityPoint(300, 1f),
        IntensityPoint(300, 0f),
        IntensityPoint(400, 0f),
      )

    val lines = generateLines(points)

    val point1 = IntensityPoint(80, 1f)
    val point2 = IntensityPoint(300, 0.2f)

    val expectedPoints =
      arrayListOf(IntensityPoint(80, 0f), points[1], points[2], points[3], points[4], points[5])

    assertEquals(expectedPoints, getIntensityFromInterval(point1.relativeTime, point2.relativeTime, lines))
  }
}
