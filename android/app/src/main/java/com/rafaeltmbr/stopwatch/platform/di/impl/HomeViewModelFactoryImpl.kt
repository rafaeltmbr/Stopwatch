package com.rafaeltmbr.stopwatch.platform.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.use_cases.LoggingUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.PauseStopwatchUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.ResetStopwatchUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.platform.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.ViewTimeMapper
import com.rafaeltmbr.stopwatch.platform.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.platform.presentation.view_models.HomeViewModel
import com.rafaeltmbr.stopwatch.platform.presentation.view_models.impl.HomeViewModelImpl

class HomeViewModelFactoryImpl(
    private val stopwatchStore: MutableStateStore<StopwatchState>,
    private val stackNavigator: StackNavigator,
    private val loggingUseCase: LoggingUseCase,
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val pauseStopwatchUseCase: PauseStopwatchUseCase,
    private val resetStopwatchUseCase: ResetStopwatchUseCase,
    private val newLapUseCase: NewLapUseCase,
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
                    loggingUseCase,
                    startStopwatchUseCase,
                    pauseStopwatchUseCase,
                    resetStopwatchUseCase,
                    newLapUseCase,
                    viewTimeMapper,
                    stringTimeMapper,
                )
            }
        }

        return viewModel<HomeViewModelImpl>(factory = factory)
    }
}