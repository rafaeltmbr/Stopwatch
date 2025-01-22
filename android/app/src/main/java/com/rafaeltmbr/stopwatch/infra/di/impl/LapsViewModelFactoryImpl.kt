package com.rafaeltmbr.stopwatch.infra.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.LoggingService
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.StringTimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.LapsViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.LapsViewModelImpl

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