package com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl

import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.ViewTimeMapper

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