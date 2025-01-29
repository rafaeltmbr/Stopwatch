package com.rafaeltmbr.stopwatch.platform.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.LoggingService
import com.rafaeltmbr.stopwatch.core.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.platform.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.platform.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.platform.presentation.view_models.LapsViewModel
import com.rafaeltmbr.stopwatch.platform.presentation.view_models.impl.LapsViewModelImpl

class LapsViewModelFactoryImpl(
    private val stopwatchStore: MutableStateStore<StopwatchState>,
    private val stackNavigator: StackNavigator,
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val newLapUseCase: NewLapUseCase,
    private val loggingService: LoggingService,
    private val stringTimeMapper: StringTimeMapper
) : LapsViewModelFactory {
    @Composable
    override fun make(): LapsViewModel {
        val factory = viewModelFactory {
            initializer {
                LapsViewModelImpl(
                    stopwatchStore,
                    stackNavigator,
                    startStopwatchUseCase,
                    newLapUseCase,
                    loggingService,
                    stringTimeMapper,
                )
            }
        }

        return viewModel<LapsViewModelImpl>(factory = factory)
    }
}