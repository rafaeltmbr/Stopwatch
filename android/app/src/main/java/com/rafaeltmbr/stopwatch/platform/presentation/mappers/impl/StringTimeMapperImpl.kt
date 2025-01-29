package com.rafaeltmbr.stopwatch.platform.presentation.mappers.impl

import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.ViewTimeMapper

class StringTimeMapperImpl(
    private val viewTimerMapper: ViewTimeMapper
) : StringTimeMapper {
    override fun map(milliseconds: Long): String {
        val viewTime: ViewTime = viewTimerMapper.map(milliseconds)
        return "%s:%s.%s".format(
            viewTime.minutes.joinToString(""),
            viewTime.seconds.joinToString(""),
            viewTime.fraction.joinToString("")
        )
    }
}