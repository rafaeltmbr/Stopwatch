package com.rafaeltmbr.stopwatch.infra.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.NewLapUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.TimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.LapsViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.LapsViewModelImpl

class LapsViewModelFactoryImpl(
    private val stopwatchStore: MutableStateStore<StopwatchState>,
    private val presentationStore: MutableStateStore<PresentationState>,
    private val stackNavigator: StackNavigator,
    private val timerService: TimerService,
) : LapsViewModelFactory {
    @Composable
    override fun make(): LapsViewModel {
        val factory = viewModelFactory {
            initializer {
                val timeMapper = TimeMapper()
                val startStopwatchUseCase = StartStopwatchUseCaseImpl(
                    store = stopwatchStore,
                    timerService = timerService,
                )
                val newLapUseCase = NewLapUseCaseImpl(
                    store = stopwatchStore
                )
                LapsViewModelImpl(
                    stringTimeMapper = timeMapper,
                    stopwatchStore = stopwatchStore,
                    presentationStore = presentationStore,
                    startStopwatchUseCase = startStopwatchUseCase,
                    newLapUseCase = newLapUseCase,
                    stackNavigator = stackNavigator
                )
            }
        }

        return viewModel<LapsViewModelImpl>(factory = factory)
    }
}