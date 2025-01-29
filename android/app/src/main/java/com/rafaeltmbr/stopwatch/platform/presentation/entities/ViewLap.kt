package com.rafaeltmbr.stopwatch.platform.presentation.entities

import com.rafaeltmbr.stopwatch.core.entities.Lap

data class ViewLap(
    val index: Int,
    val time: String,
    val status: Lap.Status
)
