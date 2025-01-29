package com.rafaeltmbr.stopwatch.platform.di.impl

import android.content.Context
import com.rafaeltmbr.stopwatch.core.data.repositories.impl.StopwatchRepositoryImpl
import com.rafaeltmbr.stopwatch.core.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.impl.LoggingServiceImpl
import com.rafaeltmbr.stopwatch.core.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.NewLapUseCaseImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.PauseStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.ResetStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.RestoreStopwatchStateUseCaseImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.SaveStopwatchStateUseCaseImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.StartStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.UpdateStopwatchTimeUseCaseImpl
import com.rafaeltmbr.stopwatch.core.utils.impl.CalculateLapsStatusesImpl
import com.rafaeltmbr.stopwatch.platform.data.room.StopwatchDatabase
import com.rafaeltmbr.stopwatch.platform.data.room.data_sources.RoomStopwatchDataSourceAdapter
import com.rafaeltmbr.stopwatch.platform.di.ApplicationContainer
import com.rafaeltmbr.stopwatch.platform.di.ApplicationContainerFactory
import com.rafaeltmbr.stopwatch.platform.presentation.compose.navigation.StackNavigatorImpl
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.impl.StringTimeMapperImpl
import com.rafaeltmbr.stopwatch.platform.presentation.mappers.impl.ViewTimeMapperImpl
import com.rafaeltmbr.stopwatch.platform.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.platform.services.external_resources.AndroidPlatformLoggerAdapter

class ApplicationApplicationContainerFactoryImpl(private val context: Context) :
    ApplicationContainerFactory {
    override fun make(): ApplicationContainer {
        val database = StopwatchDatabase.getInstance(context)

        val data = ApplicationContainer.Data(
            MutableStateStoreImpl(StopwatchState()),
            StopwatchRepositoryImpl(
                RoomStopwatchDataSourceAdapter(
                    database.stopwatchStateDao(),
                    database.lapsDao()
                )
            )
        )

        val services = ApplicationContainer.Services(
            TimerServiceImpl(),
            LoggingServiceImpl(AndroidPlatformLoggerAdapter())
        )

        val calculateLapsStatuses = CalculateLapsStatusesImpl()
        val useCases = ApplicationContainer.UseCases(
            NewLapUseCaseImpl(data.stopwatchStore, calculateLapsStatuses),
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
            UpdateStopwatchTimeUseCaseImpl(data.stopwatchStore),
        )

        val stackNavigator = StackNavigatorImpl(listOf(StackNavigator.Screen.Home))
        val viewTimeMapper = ViewTimeMapperImpl()
        val stringTimeMapper = StringTimeMapperImpl(ViewTimeMapperImpl())

        val presentation = ApplicationContainer.Presentation(
            stackNavigator,
            HomeViewModelFactoryImpl(
                data.stopwatchStore,
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
                stackNavigator,
                useCases.startStopwatch,
                useCases.newLap,
                services.logging,
                stringTimeMapper
            ),
        )

        return ApplicationContainer(
            data,
            services,
            useCases,
            presentation
        )
    }
}