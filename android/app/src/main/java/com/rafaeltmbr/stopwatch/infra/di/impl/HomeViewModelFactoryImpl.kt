package com.rafaeltmbr.stopwatch.infra.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.LoggingService
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.PauseStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.ResetStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.infra.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.ViewTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.HomeViewModelImpl

class HomeViewModelFactoryImpl(
    private val stopwatchStore: MutableStateStore<StopwatchState>,
    private val stackNavigator: StackNavigator,
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val pauseStopwatchUseCase: PauseStopwatchUseCase,
    private val resetStopwatchUseCase: ResetStopwatchUseCase,
    private val newLapUseCase: NewLapUseCase,
    private val loggingService: LoggingService,
    private val viewTimeMapper: ViewTimeMapper,
    private val stringTimeMapper: StringTimeMapper
) : HomeViewModelFactory {
    @Composable
    override fun make(
    ): HomeViewModel {
        val factory = viewModelFactory {
            initializer {
                HomeViewModelImpl(
                    stopwatchStore,
                    stackNavigator,
                    startStopwatchUseCase,
                    pauseStopwatchUseCase,
                    resetStopwatchUseCase,
                    newLapUseCase,
                    loggingService,
                    viewTimeMapper,
                    stringTimeMapper,
                )
            }
        }

        return viewModel<HomeViewModelImpl>(factory = factory)
    }
}