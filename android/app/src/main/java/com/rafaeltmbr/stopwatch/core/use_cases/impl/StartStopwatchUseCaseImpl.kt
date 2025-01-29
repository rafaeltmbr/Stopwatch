package com.rafaeltmbr.stopwatch.core.use_cases.impl

import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.TimerService
import com.rafaeltmbr.stopwatch.core.use_cases.StartStopwatchUseCase

class StartStopwatchUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val timerService: TimerService,
) : StartStopwatchUseCase {
    override suspend fun execute() {
        if (timerService.state.value.isRunning) return

        store.update { it.copy(status = Status.RUNNING) }
        timerService.start()
    }
}