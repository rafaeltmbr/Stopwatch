package com.rafaeltmbr.stopwatch.infra

import LapsViewModelFactoryImpl
import android.app.Application
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.UpdateStopwatchTimeAndLapUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.impl.HomeViewModelFactoryImpl
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.entities.Screen
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.impl.StackNavigatorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ApplicationContainer(
    val homeViewModelFactory: HomeViewModelFactory,
    val lapsViewModelFactory: LapsViewModelFactory,
    val stackNavigator: StackNavigatorImpl
)

class Stopwatch : Application() {
    lateinit var container: ApplicationContainer

    override fun onCreate() {
        super.onCreate()

        val stopwatchStore = MutableStateStoreImpl(StopwatchState())
        val presentationStore =
            MutableStateStoreImpl(PresentationState(screens = listOf(Screen.Home)))
        val stackNavigator = StackNavigatorImpl(presentationStore)
        val timerService = TimerServiceImpl()

        val updateStopwatchTimeAndLapUseCase =
            UpdateStopwatchTimeAndLapUseCaseImpl(stopwatchStore)

        CoroutineScope(Dispatchers.Main).launch {
            timerService.state.collect(updateStopwatchTimeAndLapUseCase::execute)
        }

        container = ApplicationContainer(
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
        )
    }
}