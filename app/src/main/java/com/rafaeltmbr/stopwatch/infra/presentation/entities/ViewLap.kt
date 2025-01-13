package com.rafaeltmbr.stopwatch.infra.presentation.entities

import com.rafaeltmbr.stopwatch.domain.entities.LapStatus

data class ViewLap(
    val index: Int,
    val milliseconds: String,
    val status: LapStatus
)
