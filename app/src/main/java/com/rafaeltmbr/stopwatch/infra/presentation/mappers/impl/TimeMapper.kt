package com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl

import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.ViewTimeMapper
import kotlin.math.max
import kotlin.math.round

class TimeMapper : ViewTimeMapper, StringTimeMapper {
    override fun mapToViewTime(milliseconds: Long): ViewTime {
        val time = max(milliseconds, 0L)

        val fraction = "%02d".format((round(time / 10f).toInt() % 100).toInt())
        val seconds = "%02d".format((time / 1000 % 60).toInt())
        val minutes = "%02d".format((time / 60_000).toInt())

        return ViewTime(
            minutes = minutes.split("").filter { it.isNotEmpty() },
            seconds = seconds.split("").filter { it.isNotEmpty() },
            fraction = fraction.split("").filter { it.isNotEmpty() }
        )
    }

    override fun mapToStringTime(milliseconds: Long): String {
        val viewTime: ViewTime = this.mapToViewTime(milliseconds)
        return "%s:%s.%s".format(
            viewTime.minutes.joinToString(""),
            viewTime.seconds.joinToString(""),
            viewTime.fraction.joinToString("")
        )
    }
}