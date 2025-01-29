package com.rafaeltmbr.stopwatch.core.entities

data class StopwatchState(
    val status: Status = Status.INITIAL,
    val milliseconds: Long = 0L,
    val completedLaps: List<Lap> = emptyList(),
    val completedLapsMilliseconds: Long = 0L,
)
