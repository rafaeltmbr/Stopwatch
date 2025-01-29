package com.rafaeltmbr.stopwatch.core.use_cases

import com.rafaeltmbr.stopwatch.core.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.core.entities.Lap
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.TimerService
import com.rafaeltmbr.stopwatch.core.use_cases.impl.UpdateStopwatchTimeUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UpdateStopwatchAppTimeUseCaseImplTest {
    @Test
    fun update_shouldCorrectlyUpdateStopwatchState() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 1_000L,
                completedLaps = listOf(
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = Lap.Status.DONE
                    )
                ),
                completedLapsMilliseconds = 1_000L
            )
        )
        val useCase = UpdateStopwatchTimeUseCaseImpl(store)

        useCase.execute(
            timerState = TimerService.State(
                isRunning = true,
                milliseconds = 1_700L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 1_700L,
            completedLaps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = Lap.Status.DONE
                )
            ),
            completedLapsMilliseconds = 1_000L
        )

        Assert.assertEquals(expected, store.state.value)
    }
}