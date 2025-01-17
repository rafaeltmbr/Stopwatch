package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase

class StartStopwatchUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val timerService: TimerService,
) : StartStopwatchUseCase {
    override suspend fun execute() {
        if (timerService.state.value.isRunning) return

        timerService.start()
        store.update { it.copy(status = Status.RUNNING) }
    }
}