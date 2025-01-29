package com.rafaeltmbr.stopwatch.platform.presentation.view_models.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaeltmbr.stopwatch.core.data.stores.StateStore
import com.rafaeltmbr.stopwatch.core.entities.Lap
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.use_cases.LoggingUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.platform.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.platform.presentation.view_models.LapsViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "LapsViewModelImpl"


class LapsViewModelImpl(
    private val stopwatchStore: StateStore<StopwatchState>,
    private val stackNavigator: StackNavigator,
    private val loggingUseCase: LoggingUseCase,
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val newLapUseCase: NewLapUseCase,
    private val stringTimeMapper: StringTimeMapper,
) : ViewModel(), LapsViewModel {
    private var _state = MutableStateFlow(LapsViewModel.State())

    init {
        viewModelScope.launch {
            stopwatchStore.state.collect(::handleStopwatchStateUpdate)
        }
    }

    override val state: StateFlow<LapsViewModel.State> = _state.asStateFlow()

    override fun handleAction(action: LapsViewModel.Action) {
        viewModelScope.launch {
            try {
                when (action) {
                    LapsViewModel.Action.Resume -> startStopwatchUseCase.execute()
                    LapsViewModel.Action.Lap -> newLapUseCase.execute()
                    LapsViewModel.Action.NavigateBack -> stackNavigator.pop()
                }
                loggingUseCase.debug(TAG, "Action handled: $action")
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                loggingUseCase.error(TAG, "Failed to handle action: $action", e)
            }
        }
    }

    override fun getViewLapByReversedArrayIndex(index: Int): ViewLap {
        val stopwatchState = stopwatchStore.state.value
        val computedIndex = stopwatchState.completedLaps.size - index
        val lap = stopwatchState.completedLaps.getOrNull(computedIndex) ?: Lap(
            index = stopwatchState.completedLaps.size + 1,
            milliseconds = stopwatchState.milliseconds - stopwatchState.completedLapsMilliseconds,
            status = Lap.Status.CURRENT
        )

        return ViewLap(
            index = lap.index,
            time = stringTimeMapper.map(lap.milliseconds),
            status = lap.status
        )
    }

    private fun handleStopwatchStateUpdate(stopwatchState: StopwatchState) {
        _state.update { currentState ->
            currentState.copy(
                status = stopwatchState.status,
                lapsCount = stopwatchState.completedLaps.size + 1,
                time = stringTimeMapper.map(stopwatchState.milliseconds),
            )
        }
    }
}