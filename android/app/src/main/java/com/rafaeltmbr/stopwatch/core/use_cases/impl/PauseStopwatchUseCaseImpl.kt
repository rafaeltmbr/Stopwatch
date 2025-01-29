package com.rafaeltmbr.stopwatch.core.use_cases.impl

import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.TimerService
import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.use_cases.PauseStopwatchUseCase

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