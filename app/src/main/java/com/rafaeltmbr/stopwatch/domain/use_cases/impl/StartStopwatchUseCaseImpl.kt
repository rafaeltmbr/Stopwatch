package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartStopwatchUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val timerService: TimerService,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : StartStopwatchUseCase {
    init {
        coroutineScope.launch {
            timerService.state.collect { timerState ->
                store.update { it.copy(milliseconds = timerState.milliseconds) }
            }
        }
    }

    override suspend fun execute() {
        if (store.state.value.status == Status.RUNNING) return

        timerService.start()
        store.update { it.copy(status = Status.RUNNING) }
    }
}