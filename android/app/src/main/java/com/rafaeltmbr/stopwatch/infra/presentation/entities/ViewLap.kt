package com.rafaeltmbr.stopwatch.infra.presentation.entities

import com.rafaeltmbr.stopwatch.domain.entities.Lap

data class ViewLap(
    val index: Int,
    val time: String,
    val status: Lap.Status
)
