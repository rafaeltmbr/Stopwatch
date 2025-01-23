package com.rafaeltmbr.stopwatch.domain.use_cases

import com.rafaeltmbr.stopwatch.domain.services.TimerService


interface UpdateStopwatchTimeUseCase {
    suspend fun execute(timerState: TimerService.State)
}