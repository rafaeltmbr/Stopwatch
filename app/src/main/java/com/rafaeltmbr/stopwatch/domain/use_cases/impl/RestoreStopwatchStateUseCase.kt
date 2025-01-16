package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.RestoreStopwatchStateUseCase

class RestoreStopwatchStateUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val repository: StopwatchRepository,
    private val timer: TimerService
) : RestoreStopwatchStateUseCase {
    override suspend fun execute() {
        val state = repository.load() ?: return

        if (state.milliseconds == 0L || state.laps.isEmpty()) return

        store.update {
            state.copy(
                status = Status.PAUSED,
                laps = state.laps + Lap(index = state.laps.size + 1)
            )
        }

        timer.set(TimerService.State(milliseconds = state.milliseconds, isRunning = false))

    }
}