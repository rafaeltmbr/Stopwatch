package com.rafaeltmbr.stopwatch.core.use_cases.impl

import com.rafaeltmbr.stopwatch.core.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.entities.Time
import com.rafaeltmbr.stopwatch.core.services.TimerService
import com.rafaeltmbr.stopwatch.core.use_cases.RestoreStopwatchStateUseCase

class RestoreStopwatchStateUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val repository: StopwatchRepository,
    private val timerService: TimerService,
) : RestoreStopwatchStateUseCase {
    override suspend fun execute() {
        val state = repository.load() ?: return

        if (state.time == Time()) return

        store.update {
            state.pause()
        }

        timerService.set(
            TimerService.State(
                milliseconds = state.time.milliseconds,
                isRunning = false
            )
        )
    }
}