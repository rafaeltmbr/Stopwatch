package com.rafaeltmbr.stopwatch.infra.di

import com.rafaeltmbr.stopwatch.domain.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.LoggingService
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.PauseStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.ResetStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.RestoreStopwatchStateUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.SaveStopwatchStateUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.UpdateStopwatchTimeUseCase
import com.rafaeltmbr.stopwatch.infra.presentation.compose.navigation.StackNavigatorImpl

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
        val newLap: NewLapUseCase,
        val pauseStopwatch: PauseStopwatchUseCase,
        val resetStopwatch: ResetStopwatchUseCase,
        val restoreStopwatchState: RestoreStopwatchStateUseCase,
        val saveStopwatchState: SaveStopwatchStateUseCase,
        val startStopwatch: StartStopwatchUseCase,
        val updateStopwatchTimeAndLap: UpdateStopwatchTimeUseCase,
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