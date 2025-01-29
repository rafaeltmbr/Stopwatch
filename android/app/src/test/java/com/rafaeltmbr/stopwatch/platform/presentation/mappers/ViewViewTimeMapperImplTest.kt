package com.rafaeltmbr.stopwatch.platform.presentation.mappers

import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.impl.ViewTimeMapperImpl
import org.junit.Assert
import org.junit.Test

class ViewViewTimeMapperImplTest {
    @Test
    fun mapFractions_paddingMinutesAndSecondsWithZeros() {
        val mapper = ViewTimeMapperImpl()
        val milliseconds = 760L
        val expected = ViewTime(
            minutes = listOf("0", "0"),
            seconds = listOf("0", "0"),
            fraction = listOf("7", "6")
        )
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToViewTimeFractions_roundToClosestHundredthPart() {
        val mapper = ViewTimeMapperImpl()
        val milliseconds = 477L
        val expected = ViewTime(
            minutes = listOf("0", "0"),
            seconds = listOf("0", "0"),
            fraction = listOf("4", "8")
        )
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapSeconds_paddingMinutesWithZeros() {
        val mapper = ViewTimeMapperImpl()
        val milliseconds = 21520L
        val expected = ViewTime(
            minutes = listOf("0", "0"),
            seconds = listOf("2", "1"),
            fraction = listOf("5", "2")
        )
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapMinutes_paddingTwoDigitsWithZeros() {
        val mapper = ViewTimeMapperImpl()
        val milliseconds = 107120L
        val expected = ViewTime(
            minutes = listOf("0", "1"),
            seconds = listOf("4", "7"),
            fraction = listOf("1", "2")
        )
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapMinutes_shouldNotTruncateMinutesIntoHours() {
        val mapper = ViewTimeMapperImpl()
        val milliseconds = 4107430L
        val expected = ViewTime(
            minutes = listOf("6", "8"),
            seconds = listOf("2", "7"),
            fraction = listOf("4", "3")
        )
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToViewTimeNegativeTime_shouldSetTimeToZero() {
        val mapper = ViewTimeMapperImpl()
        val milliseconds = -4107430L
        val expected = ViewTime(
            minutes = listOf("0", "0"),
            seconds = listOf("0", "0"),
            fraction = listOf("0", "0")
        )
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }
}