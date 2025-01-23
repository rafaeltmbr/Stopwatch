package com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaeltmbr.stopwatch.domain.data.stores.StateStore
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.LoggingService
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.PauseStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.ResetStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.ViewTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModelImpl"

class HomeViewModelImpl(
    stopwatchStore: StateStore<StopwatchState>,
    private val stackNavigator: StackNavigator,
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val pauseStopwatchUseCase: PauseStopwatchUseCase,
    private val resetStopwatchUseCase: ResetStopwatchUseCase,
    private val newLapUseCase: NewLapUseCase,
    private val loggingService: LoggingService,
    private val viewTimeMapper: ViewTimeMapper,
    private val stringTimeMapper: StringTimeMapper,
) : ViewModel(), HomeViewModel {
    private var _state: MutableStateFlow<HomeViewModel.State> =
        MutableStateFlow(HomeViewModel.State())

    init {
        viewModelScope.launch {
            stopwatchStore.state.collect(::handleStopwatchStateUpdate)
        }
    }

    override val state: StateFlow<HomeViewModel.State>
        get() = _state.asStateFlow()

    override fun handleAction(action: HomeViewModel.Action) {
        viewModelScope.launch {
            try {
                when (action) {
                    HomeViewModel.Action.Start -> startStopwatchUseCase.execute()
                    HomeViewModel.Action.Pause -> pauseStopwatchUseCase.execute()
                    HomeViewModel.Action.Resume -> startStopwatchUseCase.execute()
                    HomeViewModel.Action.Reset -> resetStopwatchUseCase.execute()
                    HomeViewModel.Action.Lap -> newLapUseCase.execute()
                    HomeViewModel.Action.SeeAll -> stackNavigator.push(StackNavigator.Screen.Laps)
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
                    .subList(
                        fromIndex = 0,
                        toIndex = minOf(stopwatchState.laps.size, 3)
                    )
                    .map {
                        ViewLap(
                            index = it.index,
                            time = stringTimeMapper.mapToStringTime(it.milliseconds),
                            status = it.status
                        )
                    },
                time = viewTimeMapper.mapToViewTime(stopwatchState.milliseconds),
                showLapsSection = stopwatchState.status != Status.INITIAL,
                showSeeAllLaps = stopwatchState.laps.size > 3,
            )
        }
    }
}