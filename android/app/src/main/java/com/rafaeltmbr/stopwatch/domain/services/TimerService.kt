package com.rafaeltmbr.stopwatch.domain.services

import kotlinx.coroutines.flow.StateFlow


interface TimerService {
    data class State(
        val isRunning: Boolean,
        val milliseconds: Long
    )

    val state: StateFlow<State>

    fun set(state: State)
    fun start()
    fun pause()
    fun reset()
}