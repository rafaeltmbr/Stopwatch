package com.rafaeltmbr.stopwatch.infra.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.di.ViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.TimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.HomeViewModelImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ViewModelFactoryImpl(
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : ViewModelFactory {
    @Composable
    override fun makeHomeViewModel(
    ): HomeViewModel {
        val stopwatchStore = MutableStateStoreImpl(
            StopwatchState(
                status = Status.INITIAL,
                milliseconds = 0L,
                laps = emptyList()
            )
        )

        val timeMapper = TimeMapper()
        val timerService = TimerServiceImpl(coroutineScope)
        val startStopwatchUseCase = StartStopwatchUseCaseImpl(
            store = stopwatchStore,
            timerService = timerService,
            coroutineScope
        )
        val factory = viewModelFactory {
            initializer {
                HomeViewModelImpl(
                    viewTimeMapper = timeMapper,
                    stringTimeMapper = timeMapper,
                    stopwatchStore = stopwatchStore,
                    startStopwatchUseCase = startStopwatchUseCase
                )

            }
        }

        return viewModel<HomeViewModelImpl>(factory = factory)
    }
}