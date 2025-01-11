package com.rafaeltmbr.stopwatch.domain.entities

data class StopwatchState(
    val status: Status,
    val milliseconds: Long,
    val laps: List<Lap>
)
