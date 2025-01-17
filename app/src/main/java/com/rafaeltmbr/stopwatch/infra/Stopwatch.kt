package com.rafaeltmbr.stopwatch.infra

import android.app.Application
import com.rafaeltmbr.stopwatch.domain.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.domain.data.repositories.impl.StopwatchRepositoryImpl
import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.LoggingService
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.services.impl.LoggingServiceImpl
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.PauseStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.ResetStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.RestoreStopwatchStateUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.SaveStopwatchStateUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.StartStopwatchUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.UpdateStopwatchTimeAndLapUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.NewLapUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.PauseStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.ResetStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.RestoreStopwatchStateUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.SaveStopwatchStateUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.UpdateStopwatchTimeAndLapUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.data.room.StopwatchDatabase
import com.rafaeltmbr.stopwatch.infra.data.room.data_sources.StopwatchDataSourceRoom
import com.rafaeltmbr.stopwatch.infra.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.impl.HomeViewModelFactoryImpl
import com.rafaeltmbr.stopwatch.infra.di.impl.LapsViewModelFactoryImpl
import com.rafaeltmbr.stopwatch.infra.presentation.compose.navigation.StackNavigatorImpl
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.entities.Screen
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.StringTimeMapperImpl
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.ViewTimeMapperImpl
import com.rafaeltmbr.stopwatch.infra.services.external_resources.AndroidLoggerFacade
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

        createContainer()
        restoreStopwatchState()
    }

    private fun createContainer() {
        val database = StopwatchDatabase.getInstance(this)

        val data = Container.Data(
            MutableStateStoreImpl(StopwatchState()),
            MutableStateStoreImpl(PresentationState(screens = listOf(Screen.Home))),
            StopwatchRepositoryImpl(
                StopwatchDataSourceRoom(
                    database.stopwatchStateDao(),
                    database.lapsDao()
                )
            )
        )

        val services = Container.Services(
            TimerServiceImpl(),
            LoggingServiceImpl(AndroidLoggerFacade())
        )

        val useCases = Container.UseCases(
            NewLapUseCaseImpl(data.stopwatchStore),
            PauseStopwatchUseCaseImpl(data.stopwatchStore, services.timer),
            ResetStopwatchUseCaseImpl(data.stopwatchStore, services.timer),
            RestoreStopwatchStateUseCaseImpl(
                data.stopwatchStore,
                data.stopwatchRepository,
                services.timer,
            ),
            SaveStopwatchStateUseCaseImpl(
                data.stopwatchStore,
                data.stopwatchRepository,
            ),
            StartStopwatchUseCaseImpl(data.stopwatchStore, services.timer),
            UpdateStopwatchTimeAndLapUseCaseImpl(data.stopwatchStore),
        )

        val stackNavigator = StackNavigatorImpl(data.presentationStore)
        val viewTimeMapper = ViewTimeMapperImpl()
        val stringTimeMapper = StringTimeMapperImpl(ViewTimeMapperImpl())

        val presentation = Container.Presentation(
            stackNavigator,
            HomeViewModelFactoryImpl(
                data.stopwatchStore,
                data.presentationStore,
                stackNavigator,
                useCases.startStopwatch,
                useCases.pauseStopwatch,
                useCases.resetStopwatch,
                useCases.newLap,
                services.logging,
                viewTimeMapper,
                stringTimeMapper
            ),
            LapsViewModelFactoryImpl(
                data.stopwatchStore,
                data.presentationStore,
                stackNavigator,
                useCases.startStopwatch,
                useCases.newLap,
                services.logging,
                stringTimeMapper
            ),
        )

        container = Container(
            data,
            services,
            useCases,
            presentation
        )
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