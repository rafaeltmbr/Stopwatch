package com.rafaeltmbr.stopwatch.domain.services

import kotlinx.coroutines.flow.StateFlow

data class TimerState(
    val isRunning: Boolean,
    val milliseconds: Long
)

interface TimerService {
    val state: StateFlow<TimerState>

    fun start()
    fun pause()
    fun reset()
}