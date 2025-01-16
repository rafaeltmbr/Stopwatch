package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.PauseStopwatchUseCase

class PauseStopwatchUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val timerService: TimerService
) : PauseStopwatchUseCase {
    override suspend fun execute() {
        if (!timerService.state.value.isRunning) return

        timerService.pause()
        store.update { it.copy(status = Status.PAUSED) }
    }
}