package com.rafaeltmbr.stopwatch.infra.di.impl

import android.content.Context
import com.rafaeltmbr.stopwatch.domain.data.repositories.impl.StopwatchRepositoryImpl
import com.rafaeltmbr.stopwatch.domain.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.impl.LoggingServiceImpl
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.NewLapUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.PauseStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.ResetStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.RestoreStopwatchStateUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.SaveStopwatchStateUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.UpdateStopwatchTimeAndLapUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.Stopwatch.Container
import com.rafaeltmbr.stopwatch.infra.data.room.StopwatchDatabase
import com.rafaeltmbr.stopwatch.infra.data.room.data_sources.StopwatchDataSourceRoom
import com.rafaeltmbr.stopwatch.infra.presentation.compose.navigation.StackNavigatorImpl
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.entities.Screen
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.StringTimeMapperImpl
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.ViewTimeMapperImpl
import com.rafaeltmbr.stopwatch.infra.services.external_resources.AndroidLoggerFacade

class StopwatchContainerFactoryImpl(private val context: Context) {
    fun make(): Container {
        val database = StopwatchDatabase.getInstance(context)

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

        return Container(
            data,
            services,
            useCases,
            presentation
        )
    }
}