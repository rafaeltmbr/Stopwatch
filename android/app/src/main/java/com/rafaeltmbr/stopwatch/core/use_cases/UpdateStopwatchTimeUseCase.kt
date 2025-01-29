package com.rafaeltmbr.stopwatch.core.use_cases

import com.rafaeltmbr.stopwatch.core.services.TimerService


interface UpdateStopwatchTimeUseCase {
    suspend fun execute(timerState: TimerService.State)
}