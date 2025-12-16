package com.swmansion.pulsarapp

import com.swmansion.pulsarapp.types.IntensityPoint
import com.swmansion.pulsarapp.types.PlotPoint
import com.swmansion.pulsarapp.types.PresetPlot
import com.swmansion.pulsarapp.types.SharpnessPoint
import org.junit.Assert.assertEquals
import org.junit.Test

class AddFrequencyUnitTest {
    @Test
    fun simpleTest(){
        val points = arrayListOf(
            IntensityPoint(0, 0f),
            IntensityPoint(1000, 1f),
            IntensityPoint(2000, 0f),
            IntensityPoint(2500, 1f),
            )

        val frequencies = arrayListOf(
            SharpnessPoint(0, 1f),
            SharpnessPoint(100, 0.8f),
            SharpnessPoint(1000, 0.2f)
        )

        val plot = PresetPlot(points, frequencies)

        val expectedResult = arrayListOf(
            PlotPoint(0, 0f, 1f),
            PlotPoint(100, 0.1f, 1f),
            PlotPoint(100, 0.1f, 0.8f),
            PlotPoint(1000, 1f, 0.8f),
            PlotPoint(1000, 1f, 0.2f),
            PlotPoint(2000, 0f, 0.2f),
            PlotPoint(2500, 1f, 0.2f),
        )

        val r = convertPlotToEnvelopePoints(plot)

        assertEquals(r?.size, expectedResult.size)

        r?.let {
            for (i in 0..it.size-1){
                assertEquals(true, expectedResult[i].relativeTime == r[i].relativeTime && expectedResult[i].sharpness == r[i].sharpness && expectedResult[i].intensity == r[i].intensity)

            }
        }
    }
}