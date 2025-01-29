package com.rafaeltmbr.stopwatch.platform.presentation.mappers

import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewTime

interface ViewTimeMapper {
    fun map(milliseconds: Long): ViewTime
}