package com.swmansion.pulsarapp

import com.swmansion.pulsarapp.types.Point
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
    val points = arrayListOf(Point(0, 0f), Point(1000, 1f))

    val start = Point(0, 0f)
    val middle1 = Point(400, 0f)
    val middle2 = Point(800, 0f)
    val end = Point(1000, 0f)

    val startLinePoint = Point(0, 0f)
    val middle1LinePoint = Point(400, 0.4f)
    val middle2LinePoint = Point(800, 0.8f)
    val endLinePoint = Point(1000, 1f)

    val lines = convertPointsToLines(points)

    // start / end
    assertEquals(
      arrayListOf(startLinePoint, middle1LinePoint),
      getLinePointsFromInterval(start, middle1, lines),
    )
    assertEquals(
      arrayListOf(middle2LinePoint, endLinePoint),
      getLinePointsFromInterval(middle2, end, lines),
    )

    // middle
    assertEquals(
      arrayListOf(middle1LinePoint, middle2LinePoint),
      getLinePointsFromInterval(middle1, middle2, lines),
    )
    assertEquals(
      arrayListOf(middle2LinePoint, endLinePoint),
      getLinePointsFromInterval(middle2, end, lines),
    )

    // whole
    assertEquals(
      arrayListOf(startLinePoint, endLinePoint),
      getLinePointsFromInterval(start, end, lines),
    )
  }

  @Test
  fun multipleLinesFromStartTest() {
    val points =
      arrayListOf(
        Point(0, 0f),
        Point(200, 0.3f),
        Point(400, 0.5f),
        Point(600, 0.2f),
        Point(800, 0.7f),
        Point(1000, 0f),
      )

    val lines = convertPointsToLines(points)

    val point1 = Point(100, 1f)
    val point2 = Point(300, 1f)
    val point3 = Point(800, 1f)

    val expectedPoints1 = arrayListOf(points[0], Point(100, 0.15f))

    val expectedPoints2 = arrayListOf(points[0], points[1], Point(300, 0.4f))

    val expectedPoints3 = arrayListOf(points[0], points[1], points[2], points[3], Point(800, 0.7f))

    assertEquals(expectedPoints1, getLinePointsFromInterval(points[0], point1, lines))
    assertEquals(expectedPoints2, getLinePointsFromInterval(points[0], point2, lines))
    assertEquals(expectedPoints3, getLinePointsFromInterval(points[0], point3, lines))
  }

  @Test
  fun multipleLinesFromEndTest() {
    val points =
      arrayListOf(
        Point(0, 0f),
        Point(200, 0.3f),
        Point(400, 0.5f),
        Point(600, 0.2f),
        Point(800, 0.7f),
        Point(1000, 0f),
      )

    val lines = convertPointsToLines(points)

    val point1 = Point(100, 1f)
    val point2 = Point(300, 1f)
    val point3 = Point(800, 1f)

    val expectedPoints1 =
      arrayListOf(Point(100, 0.15f), points[1], points[2], points[3], points[4], points[5])

    val expectedPoints2 = arrayListOf(Point(300, 0.4f), points[2], points[3], points[4], points[5])

    val expectedPoints3 = arrayListOf(points[4], points[5])

    assertEquals(expectedPoints1, getLinePointsFromInterval(point1, points[5], lines))
    assertEquals(expectedPoints2, getLinePointsFromInterval(point2, points[5], lines))
    assertEquals(expectedPoints3, getLinePointsFromInterval(point3, points[5], lines))
  }

  @Test
  fun verticalLineStartEndTest() {
    val points =
      arrayListOf(
        Point(0, 0f),
        Point(100, 0.5f),
        Point(200, 0.5f),
        Point(200, 0f),
        Point(300, 0f),
        Point(300, 0.5f),
        Point(400, 0.5f),
        Point(400, 0f),
      )

    val lines = convertPointsToLines(points)

    val start = Point(200L, 1f)
    val middle = Point(250L, 1f)
    val end = Point(300L, 1f)

    val startLinePoint = points[3]
    val middleLinePoint = Point(250, 0f)
    val endLinePoint = points[4]

    assertEquals(
      arrayListOf(startLinePoint, middleLinePoint),
      getLinePointsFromInterval(start, middle, lines),
    )
    assertEquals(
      arrayListOf(middleLinePoint, endLinePoint),
      getLinePointsFromInterval(middle, end, lines),
    )
    assertEquals(
      arrayListOf(startLinePoint, endLinePoint),
      getLinePointsFromInterval(start, end, lines),
    )
  }

  @Test
  fun multipleVerticalAndHorizontalLineTest() {
    val points =
      arrayListOf(
        Point(0, 0f),
        Point(100, 0f),
        Point(100, 0.5f),
        Point(200, 0.5f),
        Point(200, 1f),
        Point(300, 1f),
        Point(300, 0f),
        Point(400, 0f),
      )

    val lines = convertPointsToLines(points)

    val point1 = Point(80, 1f)
    val point2 = Point(300, 0.2f)

    val expectedPoints =
      arrayListOf(Point(80, 0f), points[1], points[2], points[3], points[4], points[5])

    assertEquals(expectedPoints, getLinePointsFromInterval(point1, point2, lines))
  }
}
