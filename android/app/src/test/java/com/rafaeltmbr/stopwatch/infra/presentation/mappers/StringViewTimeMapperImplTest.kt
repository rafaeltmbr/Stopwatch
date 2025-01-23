package com.rafaeltmbr.stopwatch.infra.presentation.mappers

import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.StringTimeMapperImpl
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.ViewTimeMapperImpl
import org.junit.Assert
import org.junit.Test

class StringStringTimeMapperImplTest {
    @Test
    fun mapFractions_paddingMinutesAndSecondsWithZeros() {
        val mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        val milliseconds = 760L
        val expected = "00:00.76"
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToStringTimeFractions_roundToClosestHundredthPart() {
        val mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        val milliseconds = 477L
        val expected = "00:00.48"
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapSeconds_paddingMinutesWithZeros() {
        val mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        val milliseconds = 21520L
        val expected = "00:21.52"
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapMinutes_paddingTwoDigitsWithZeros() {
        val mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        val milliseconds = 107120L
        val expected = "01:47.12"
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapMinutes_shouldNotTruncateMinutesIntoHours() {
        val mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        val milliseconds = 4107430L
        val expected = "68:27.43"
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }

    @Test
    fun mapToStringTimeNegativeTime_shouldSetTimeToZero() {
        val mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        val milliseconds = -4107430L
        val expected = "00:00.00"
        val viewTime = mapper.map(milliseconds)
        Assert.assertEquals(expected, viewTime)
    }
}