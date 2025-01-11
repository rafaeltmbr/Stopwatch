package com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl

import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.ViewTimeMapper
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.round

class TimeMapper : ViewTimeMapper, StringTimeMapper {
    override fun mapToViewTime(seconds: Float): ViewTime {
        val time = max(seconds, 0f)

        val fractionString = "%02d".format(round((time - floor(time)) * 100f).toInt())
        val secondsString = "%02d".format((time % 60).toInt())
        val minutesString = "%02d".format(floor(time / 60).toInt())

        return ViewTime(
            minutes = minutesString.split("").filter { it.isNotEmpty() },
            seconds = secondsString.split("").filter { it.isNotEmpty() },
            fraction = fractionString.split("").filter { it.isNotEmpty() }
        )
    }

    override fun mapToStringTime(seconds: Float): String {
        val viewTime: ViewTime = this.mapToViewTime(seconds)
        return "%s:%s.%s".format(
            viewTime.minutes.joinToString(""),
            viewTime.seconds.joinToString(""),
            viewTime.fraction.joinToString("")
        )
    }
}