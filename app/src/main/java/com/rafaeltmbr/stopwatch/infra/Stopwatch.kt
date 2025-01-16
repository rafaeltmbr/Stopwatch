package com.rafaeltmbr.stopwatch.infra

import android.app.Application
import com.rafaeltmbr.stopwatch.domain.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.domain.data.repositories.impl.StopwatchRepositoryImpl
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.SaveStopwatchStateUseCase
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.RestoreStopwatchStateUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.SaveStopwatchStateUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.UpdateStopwatchTimeAndLapUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.data.StopwatchDatabase
import com.rafaeltmbr.stopwatch.infra.data.data_sources.StopwatchDataSourceImpl
import com.rafaeltmbr.stopwatch.infra.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.impl.HomeViewModelFactoryImpl
import com.rafaeltmbr.stopwatch.infra.di.impl.LapsViewModelFactoryImpl
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.entities.Screen
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.impl.StackNavigatorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Stopwatch : Application() {
    data class Container(
        val stopwatchStore: MutableStateStore<StopwatchState>,
        val presentationStore: MutableStateStore<PresentationState>,
        val homeViewModelFactory: HomeViewModelFactory,
        val lapsViewModelFactory: LapsViewModelFactory,
        val stackNavigator: StackNavigatorImpl,
        val stopwatchRepository: StopwatchRepository,
        val saveStopwatchStateUseCase: SaveStopwatchStateUseCase
    )

    lateinit var container: Container

    override fun onCreate() {
        super.onCreate()

        val database = StopwatchDatabase.getInstance(this)
        val stopwatchRepository = StopwatchRepositoryImpl(
            StopwatchDataSourceImpl(
                database.stopwatchStateDao(),
                database.lapsDao()
            )
        )

        val stopwatchStore = MutableStateStoreImpl(StopwatchState())
        val presentationStore =
            MutableStateStoreImpl(PresentationState(screens = listOf(Screen.Home)))
        val stackNavigator = StackNavigatorImpl(presentationStore)
        val timerService = TimerServiceImpl()

        val updateStopwatchTimeAndLapUseCase =
            UpdateStopwatchTimeAndLapUseCaseImpl(stopwatchStore)

        val restoreStopwatchStateUseCase = RestoreStopwatchStateUseCaseImpl(
            store = stopwatchStore,
            repository = stopwatchRepository,
            timer = timerService
        )

        val saveStopwatchStateUseCase = SaveStopwatchStateUseCaseImpl(
            store = stopwatchStore,
            repository = stopwatchRepository
        )

        CoroutineScope(Dispatchers.Default).launch {
            restoreStopwatchStateUseCase.execute()
            timerService.state.collect(updateStopwatchTimeAndLapUseCase::execute)
        }

        container = Container(
            stopwatchStore = stopwatchStore,
            presentationStore = presentationStore,
            homeViewModelFactory = HomeViewModelFactoryImpl(
                stopwatchStore,
                presentationStore,
                stackNavigator,
                timerService
            ),
            lapsViewModelFactory = LapsViewModelFactoryImpl(
                stopwatchStore,
                presentationStore,
                stackNavigator,
                timerService
            ),
            stackNavigator = stackNavigator,
            stopwatchRepository = stopwatchRepository,
            saveStopwatchStateUseCase = saveStopwatchStateUseCase
        )
    }
}