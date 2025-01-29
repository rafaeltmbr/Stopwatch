package com.rafaeltmbr.stopwatch.core.use_cases.impl

import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.TimerService
import com.rafaeltmbr.stopwatch.core.use_cases.ResetStopwatchUseCase

class ResetStopwatchUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val timerService: TimerService
) : ResetStopwatchUseCase {
    override suspend fun execute() {
        if (
            timerService.state.value.isRunning || store.state.value.status != Status.PAUSED
        ) return

        timerService.reset()
        store.update {
            it.copy(
                status = Status.INITIAL,
                milliseconds = 0L,
                completedLaps = emptyList(),
                completedLapsMilliseconds = 0L
            )
        }
    }
}