package com.rafaeltmbr.stopwatch.core.use_cases.impl

import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.entities.Time
import com.rafaeltmbr.stopwatch.core.services.TimerService
import com.rafaeltmbr.stopwatch.core.use_cases.UpdateStopwatchTimeUseCase

class UpdateStopwatchTimeUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
) : UpdateStopwatchTimeUseCase {
    override suspend fun execute(timerState: TimerService.State) {
        store.update {
            it.copy().updateTime(Time(timerState.milliseconds))
        }
    }
}