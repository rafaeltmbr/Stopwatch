package com.rafaeltmbr.stopwatch.domain.use_cases

import com.rafaeltmbr.stopwatch.domain.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.NewLapUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.utils.impl.CalculateLapsStatusesImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class NewLapUseCaseImplTest {
    @Test
    fun newLap_shouldCreateNewLap() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 0L,
                laps = emptyList(),
            )
        )

        val useCase = NewLapUseCaseImpl(store, CalculateLapsStatusesImpl())

        useCase.execute()

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 0L,
            laps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 0L,
                    status = Lap.Status.CURRENT
                )
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }

    @Test
    fun newLap_shouldAppendNewLapAndUpdateStatus() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 1_800L,
                laps = listOf(
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = Lap.Status.DONE
                    ),
                    Lap(
                        index = 2,
                        milliseconds = 800L,
                        status = Lap.Status.CURRENT
                    ),
                ),
            )
        )

        val useCase = NewLapUseCaseImpl(store, CalculateLapsStatusesImpl())

        useCase.execute()

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 1_800L,
            laps = listOf(
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
                Lap(
                    index = 3,
                    milliseconds = 0L,
                    status = Lap.Status.CURRENT
                ),
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }
}