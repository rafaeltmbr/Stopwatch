package com.rafaeltmbr.stopwatch.domain.entities

data class StopwatchState(
    val status: Status,
    val time: Float,
    val laps: List<Lap>
)
