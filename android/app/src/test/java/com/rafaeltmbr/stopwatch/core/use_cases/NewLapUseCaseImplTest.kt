package com.rafaeltmbr.stopwatch.core.use_cases

import com.rafaeltmbr.stopwatch.core.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.core.entities.Lap
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.use_cases.impl.NewLapUseCaseImpl
import com.rafaeltmbr.stopwatch.core.utils.impl.CalculateLapsStatusesImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class NewLapUseCaseImplTest {
    @Test
    fun newLap_shouldCreateNewLap() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 20L,
                completedLaps = emptyList(),
                completedLapsMilliseconds = 0
            )
        )

        val useCase = NewLapUseCaseImpl(store, CalculateLapsStatusesImpl())

        useCase.execute()

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 20L,
            completedLaps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 20L,
                    status = Lap.Status.DONE
                )
            ),
            completedLapsMilliseconds = 20
        )

        Assert.assertEquals(expected, store.state.value)
    }

    @Test
    fun newLap_shouldAppendNewLapAndUpdateStatus() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 1_800L,
                completedLaps = listOf(
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = Lap.Status.DONE
                    ),
                ),
                completedLapsMilliseconds = 1_000L
            )
        )

        val useCase = NewLapUseCaseImpl(store, CalculateLapsStatusesImpl())

        useCase.execute()

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 1_800L,
            completedLaps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = Lap.Status.WORST
                ),
                Lap(
                    index = 2,
                    milliseconds = 800L,
                    status = Lap.Status.BEST
                ),
            ),
            completedLapsMilliseconds = 1_800L
        )

        Assert.assertEquals(expected, store.state.value)
    }
}