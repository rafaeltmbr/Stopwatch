package com.rafaeltmbr.stopwatch.infra.presentation.mappers

import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime

interface ViewTimeMapper {
    fun mapToViewTime(seconds: Float): ViewTime
}