package com.rafaeltmbr.stopwatch.domain.services.impl

import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.services.TimerState
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
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : TimerService {
    private val _state = MutableStateFlow(TimerState(isRunning = false, milliseconds = 0L))
    private var timerJob: Job? = null

    override val state: StateFlow<TimerState> = _state.asStateFlow()

    override fun start() {
        if (_state.value.isRunning) return

        _state.update { it.copy(isRunning = true) }

        timerJob = coroutineScope.launch {
            while (_state.value.isRunning) {
                val startTime = System.currentTimeMillis()
                delay(10)

                val timeDiff = System.currentTimeMillis() - startTime
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

        _state.update { TimerState(isRunning = false, milliseconds = 0L) }
    }
}