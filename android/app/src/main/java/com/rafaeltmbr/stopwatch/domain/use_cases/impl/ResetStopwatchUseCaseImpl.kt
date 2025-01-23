package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.use_cases.ResetStopwatchUseCase

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