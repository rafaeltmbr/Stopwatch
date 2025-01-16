package com.rafaeltmbr.stopwatch.domain.services.impl

import com.rafaeltmbr.stopwatch.domain.services.TimerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerServiceImpl(
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val currentMillisecondsCallback: () -> Long = System::currentTimeMillis
) : TimerService {
    private val _state = MutableStateFlow(TimerService.State(isRunning = false, milliseconds = 0L))
    private var timerJob: Job? = null

    override val state: StateFlow<TimerService.State> = _state.asStateFlow()

    override fun set(state: TimerService.State) {
        _state.update { state }
    }

    override fun start() {
        if (_state.value.isRunning) return

        _state.update { it.copy(isRunning = true) }

        timerJob = coroutineScope.launch {
            while (_state.value.isRunning) {
                val startTime = currentMillisecondsCallback()
                delay(10)

                val timeDiff = currentMillisecondsCallback() - startTime
                _state.update { it.copy(milliseconds = it.milliseconds + timeDiff) }
            }
        }
    }

    override fun pause() {
        if (!_state.value.isRunning) return

        timerJob?.cancel()
        timerJob = null
        _state.update { it.copy(isRunning = false) }
    }

    override fun reset() {
        if (_state.value.isRunning) return

        _state.update { TimerService.State(isRunning = false, milliseconds = 0L) }
    }
}