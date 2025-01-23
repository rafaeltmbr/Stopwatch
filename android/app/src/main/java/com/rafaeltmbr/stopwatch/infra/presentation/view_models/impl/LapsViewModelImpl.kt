package com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaeltmbr.stopwatch.domain.data.stores.StateStore
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.LoggingService
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.LapsViewModel
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
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val newLapUseCase: NewLapUseCase,
    private val loggingService: LoggingService,
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
                loggingService.debug(TAG, "Action handled: $action")
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                loggingService.error(TAG, "Failed to handle action: $action", e)
            }
        }
    }

    private fun handleStopwatchStateUpdate(stopwatchState: StopwatchState) {
        _state.update { currentState ->
            currentState.copy(
                status = stopwatchState.status,
                laps = stopwatchState.laps
                    .reversed()
                    .map {
                        ViewLap(
                            index = it.index,
                            time = stringTimeMapper.mapToStringTime(it.milliseconds),
                            status = it.status
                        )
                    },
                time = stringTimeMapper.mapToStringTime(stopwatchState.milliseconds),
            )
        }
    }
}