package com.rafaeltmbr.stopwatch.infra

import android.app.Application
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
import com.rafaeltmbr.stopwatch.domain.use_cases.UpdateStopwatchTimeAndLapUseCase
import com.rafaeltmbr.stopwatch.infra.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.impl.StopwatchContainerFactoryImpl
import com.rafaeltmbr.stopwatch.infra.presentation.compose.navigation.StackNavigatorImpl
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "Stopwatch"

class Stopwatch : Application() {
    data class Container(
        val data: Data,
        val services: Services,
        val useCases: UseCases,
        val presentation: Presentation
    ) {
        data class Data(
            val stopwatchStore: MutableStateStore<StopwatchState>,
            val presentationStore: MutableStateStore<PresentationState>,
            val stopwatchRepository: StopwatchRepository,
        )

        data class UseCases(
            val newLap: NewLapUseCase,
            val pauseStopwatch: PauseStopwatchUseCase,
            val resetStopwatch: ResetStopwatchUseCase,
            val restoreStopwatchState: RestoreStopwatchStateUseCase,
            val saveStopwatchState: SaveStopwatchStateUseCase,
            val startStopwatch: StartStopwatchUseCase,
            val updateStopwatchTimeAndLap: UpdateStopwatchTimeAndLapUseCase,
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

    lateinit var container: Container

    override fun onCreate() {
        super.onCreate()

        container = StopwatchContainerFactoryImpl(this).make()
        restoreStopwatchState()
    }

    private fun restoreStopwatchState() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                container.useCases.restoreStopwatchState.execute()
                container.services.timer.state.collect(
                    container.useCases.updateStopwatchTimeAndLap::execute
                )
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                container.services.logging.error(TAG, "Failed to restore stopwatch state", e)
            }
        }
    }
}