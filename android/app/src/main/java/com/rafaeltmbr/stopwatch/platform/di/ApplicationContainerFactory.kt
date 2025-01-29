package com.rafaeltmbr.stopwatch.platform.di

import com.rafaeltmbr.stopwatch.core.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.LoggingService
import com.rafaeltmbr.stopwatch.core.services.TimerService
import com.rafaeltmbr.stopwatch.core.use_cases.LoggingUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.PauseStopwatchUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.ResetStopwatchUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.RestoreStopwatchStateUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.SaveStopwatchStateUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.core.use_cases.UpdateStopwatchTimeUseCase
import com.rafaeltmbr.stopwatch.platform.presentation.compose.navigation.StackNavigatorImpl

data class ApplicationContainer(
    val data: Data,
    val services: Services,
    val useCases: UseCases,
    val presentation: Presentation
) {
    data class Data(
        val stopwatchStore: MutableStateStore<StopwatchState>,
        val stopwatchRepository: StopwatchRepository,
    )

    data class UseCases(
        val logging: LoggingUseCase,
        val newLap: NewLapUseCase,
        val pauseStopwatch: PauseStopwatchUseCase,
        val resetStopwatch: ResetStopwatchUseCase,
        val restoreStopwatchState: RestoreStopwatchStateUseCase,
        val saveStopwatchState: SaveStopwatchStateUseCase,
        val startStopwatch: StartStopwatchUseCase,
        val updateStopwatchTime: UpdateStopwatchTimeUseCase,
    )

    data class Services(
        val timer: TimerService,
        val logging: LoggingService,
    )

    data class Presentation(
        val stackNavigator: StackNavigatorImpl,
        val homeViewModelFactory: HomeViewModelFactory,
        val lapsViewModelFactory: LapsViewModelFactory,
    )
}

interface ApplicationContainerFactory {
    fun make(): ApplicationContainer
}