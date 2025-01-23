package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.use_cases.UpdateStopwatchTimeUseCase

class UpdateStopwatchTimeUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
) : UpdateStopwatchTimeUseCase {
    override suspend fun execute(timerState: TimerService.State) {
        store.update {
            it.copy(milliseconds = timerState.milliseconds)
        }
    }
}