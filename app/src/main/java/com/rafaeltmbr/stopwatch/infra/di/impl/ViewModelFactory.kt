package com.rafaeltmbr.stopwatch.infra.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.PauseStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.ResetStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.TimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.HomeViewModelImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFactory(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : HomeViewModelFactory {
    private val stopwatchStore = MutableStateStoreImpl(
        StopwatchState(
            status = Status.INITIAL,
            milliseconds = 0L,
            laps = emptyList()
        )
    )

    private val timeMapper = TimeMapper()
    private val timerService = TimerServiceImpl(coroutineScope)
    private val startStopwatchUseCase = StartStopwatchUseCaseImpl(
        store = stopwatchStore,
        timerService = timerService,
    )
    private val pauseStopwatchUseCase = PauseStopwatchUseCaseImpl(
        store = stopwatchStore,
        timerService = timerService
    )
    private val resetStopwatchUseCase = ResetStopwatchUseCaseImpl(
        store = stopwatchStore,
        timerService = timerService
    )

    init {
        coroutineScope.launch {
            timerService.state.collect { timerState ->
                stopwatchStore.update { it.copy(milliseconds = timerState.milliseconds) }
            }
        }
    }

    @Composable
    override fun makeHomeViewModel(
    ): HomeViewModel {
        val factory = viewModelFactory {
            initializer {
                HomeViewModelImpl(
                    viewTimeMapper = timeMapper,
                    stringTimeMapper = timeMapper,
                    stopwatchStore = stopwatchStore,
                    startStopwatchUseCase = startStopwatchUseCase,
                    pauseStopwatchUseCase = pauseStopwatchUseCase,
                    resetStopwatchUseCase = resetStopwatchUseCase
                )
            }
        }

        return viewModel<HomeViewModelImpl>(factory = factory)
    }
}