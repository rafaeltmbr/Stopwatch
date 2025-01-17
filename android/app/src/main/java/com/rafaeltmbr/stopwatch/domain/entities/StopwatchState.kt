package com.rafaeltmbr.stopwatch.domain.entities

data class StopwatchState(
    val status: Status = Status.INITIAL,
    val milliseconds: Long = 0L,
    val laps: List<Lap> = emptyList()
)
