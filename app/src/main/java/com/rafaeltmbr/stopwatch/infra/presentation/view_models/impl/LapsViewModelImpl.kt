package com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.stores.StateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.LapsViewAction
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.LapsViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.LapsViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LapsViewModelImpl(
    private val presentationStore: StateStore<PresentationState>,
    private val stopwatchStore: StateStore<StopwatchState>,
    private val stringTimeMapper: StringTimeMapper,
    private val stackNavigator: StackNavigator,
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val newLapUseCase: NewLapUseCase
) : ViewModel(), LapsViewModel {
    private var _state = MutableStateFlow(LapsViewState())

    init {
        viewModelScope.launch {
            stopwatchStore.state.collect(::handleStopwatchStateUpdate)
        }
    }

    override val state: StateFlow<LapsViewState> = _state.asStateFlow()

    override fun handleAction(action: LapsViewAction) {
        viewModelScope.launch {
            when (action) {
                LapsViewAction.Resume -> startStopwatchUseCase.execute()
                LapsViewAction.Lap -> newLapUseCase.execute()
                LapsViewAction.NavigateBack -> stackNavigator.pop()
            }
        }
    }

    private fun handleStopwatchStateUpdate(stopwatchState: StopwatchState) {
        _state.update { currentState ->
            currentState.copy(
                status = stopwatchState.status,
                laps = stopwatchState.laps
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