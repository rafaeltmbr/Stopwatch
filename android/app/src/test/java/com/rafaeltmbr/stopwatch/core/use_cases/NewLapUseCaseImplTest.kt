package com.rafaeltmbr.stopwatch.core.use_cases

import com.rafaeltmbr.stopwatch.core.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.core.entities.CompletedLaps
import com.rafaeltmbr.stopwatch.core.entities.Lap
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.entities.Time
import com.rafaeltmbr.stopwatch.core.use_cases.impl.NewLapUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class NewLapUseCaseImplTest {
    @Test
    fun newLap_shouldCreateNewLap() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                time = Time(milliseconds = 20L),
                completedLaps = CompletedLaps()
            )
        )

        val useCase = NewLapUseCaseImpl(store)

        useCase.execute()

        val expected = StopwatchState(
            status = Status.RUNNING,
            time = Time(milliseconds = 20L),
            completedLaps = CompletedLaps(
                laps = listOf(
                    Lap(
                        index = 1,
                        time = Time(milliseconds = 20L),
                        status = Lap.Status.DONE
                    )
                ),
                time = Time(milliseconds = 20L)
            ),
        )

        Assert.assertEquals(expected, store.state.value)
    }

    @Test
    fun newLap_shouldAppendNewLapAndUpdateStatus() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                time = Time(milliseconds = 1_800L),
                completedLaps = CompletedLaps(
                    laps = listOf(
                        Lap(
                            index = 1,
                            time = Time(milliseconds = 1_000L),
                            status = Lap.Status.DONE
                        ),
                    ),
                    time = Time(milliseconds = 1_000L)
                )
            )
        )

        val useCase = NewLapUseCaseImpl(store)

        useCase.execute()

        val expected = StopwatchState(
            status = Status.RUNNING,
            time = Time(milliseconds = 1_800L),
            completedLaps = CompletedLaps(
                laps = listOf(
                    Lap(
                        index = 1,
                        time = Time(milliseconds = 1_000L),
                        status = Lap.Status.WORST
                    ),
                    Lap(
                        index = 2,
                        time = Time(milliseconds = 800L),
                        status = Lap.Status.BEST
                    ),
                ),
                time = Time(milliseconds = 1_800L)
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }
}