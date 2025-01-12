package com.rafaeltmbr.stopwatch.infra.presentation.view_models

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.PauseStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.ResetStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.infra.presentation.mappers.impl.TimeMapper
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.HomeViewModelImpl
import org.junit.Assert
import org.junit.Test

class HomeViewModelImplTest {
    @Test
    fun state_verifyInitialState() {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.INITIAL,
                milliseconds = 0L,
                laps = emptyList()
            )
        )

        val timeMapper = TimeMapper()
        val timerService = TimerServiceImpl()

        val viewModel = HomeViewModelImpl(
            startStopwatchUseCase = StartStopwatchUseCaseImpl(
                store = store,
                timerService = timerService
            ),
            pauseStopwatchUseCase = PauseStopwatchUseCaseImpl(
                store = store,
                timerService = timerService
            ),
            resetStopwatchUseCase = ResetStopwatchUseCaseImpl(
                store = store,
                timerService = timerService
            ),
            stopwatchStore = store,
            viewTimeMapper = timeMapper,
            stringTimeMapper = timeMapper,
        )

        val expected = HomeViewState(
            status = Status.INITIAL,
            time = ViewTime(
                minutes = listOf("0", "0"),
                seconds = listOf("0", "0"),
                fraction = listOf("0", "0"),
            ),
            laps = emptyList(),
            showSeeMore = false
        )

        Assert.assertEquals(expected, viewModel.state.value)
    }
}
