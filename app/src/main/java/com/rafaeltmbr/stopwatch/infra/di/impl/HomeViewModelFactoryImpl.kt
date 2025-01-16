package com.rafaeltmbr.stopwatch.infra.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.NewLapUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.PauseStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.ResetStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.TimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.HomeViewModelImpl

class HomeViewModelFactoryImpl(
    private val stopwatchStore: MutableStateStore<StopwatchState>,
    private val presentationStore: MutableStateStore<PresentationState>,
    private val stackNavigator: StackNavigator,
    private val timerService: TimerService,
) : HomeViewModelFactory {
    @Composable
    override fun make(
    ): HomeViewModel {
        val factory = viewModelFactory {
            initializer {
                val timeMapper = TimeMapper()
                val startStopwatchUseCase = StartStopwatchUseCaseImpl(
                    store = stopwatchStore,
                    timerService = timerService,
                )
                val pauseStopwatchUseCase = PauseStopwatchUseCaseImpl(
                    store = stopwatchStore,
                    timerService = timerService
                )
                val resetStopwatchUseCase = ResetStopwatchUseCaseImpl(
                    store = stopwatchStore,
                    timerService = timerService
                )
                val newLapUseCase = NewLapUseCaseImpl(
                    store = stopwatchStore
                )
                HomeViewModelImpl(
                    viewTimeMapper = timeMapper,
                    stringTimeMapper = timeMapper,
                    stopwatchStore = stopwatchStore,
                    presentationStore = presentationStore,
                    startStopwatchUseCase = startStopwatchUseCase,
                    pauseStopwatchUseCase = pauseStopwatchUseCase,
                    resetStopwatchUseCase = resetStopwatchUseCase,
                    newLapUseCase = newLapUseCase,
                    stackNavigator = stackNavigator
                )
            }
        }

        return viewModel<HomeViewModelImpl>(factory = factory)
    }
}