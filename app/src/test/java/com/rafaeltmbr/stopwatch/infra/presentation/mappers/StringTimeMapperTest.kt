package com.rafaeltmbr.stopwatch.infra.presentation.mappers

import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.TimeMapper
import org.junit.Assert
import org.junit.Test

class StringTimeMapperTest {
    @Test
    fun mapToStringTimeFractions_paddingMinutesAndSecondsWithZeros() {
        val mapper = TimeMapper()
        val seconds = 0.76f
        val expected = "00:00.76"
        val viewTime = mapper.mapToStringTime(seconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToStringTimeFractions_roundToClosestHundredthPart() {
        val mapper = TimeMapper()
        val seconds = 0.477f
        val expected = "00:00.48"
        val viewTime = mapper.mapToStringTime(seconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToStringTimeSeconds_paddingMinutesWithZeros() {
        val mapper = TimeMapper()
        val seconds = 21.52f
        val expected = "00:21.52"
        val viewTime = mapper.mapToStringTime(seconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToStringTimeMinutes_paddingTwoDigitsWithZeros() {
        val mapper = TimeMapper()
        val seconds = 107.12f
        val expected = "01:47.12"
        val viewTime = mapper.mapToStringTime(seconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToStringTimeMinutes_shouldNotTruncateMinutesIntoHours() {
        val mapper = TimeMapper()
        val seconds = 4107.43f
        val expected = "68:27.43"
        val viewTime = mapper.mapToStringTime(seconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToStringTimeNegativeTime_shouldSetTimeToZero() {
        val mapper = TimeMapper()
        val seconds = -4107.43f
        val expected = "00:00.00"
        val viewTime = mapper.mapToStringTime(seconds)
        Assert.assertEquals(expected, viewTime)
    }
}