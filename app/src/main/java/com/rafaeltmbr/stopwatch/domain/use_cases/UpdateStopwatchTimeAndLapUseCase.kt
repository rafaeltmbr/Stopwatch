package com.rafaeltmbr.stopwatch.domain.use_cases

import com.rafaeltmbr.stopwatch.domain.services.TimerState

interface UpdateStopwatchTimeAndLapUseCase {
    suspend fun execute(timerState: TimerState)
}