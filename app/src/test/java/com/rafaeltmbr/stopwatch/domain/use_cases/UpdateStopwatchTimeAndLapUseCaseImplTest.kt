package com.rafaeltmbr.stopwatch.domain.use_cases

import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.LapStatus
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerState
import com.rafaeltmbr.stopwatch.domain.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.UpdateStopwatchTimeAndLapUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UpdateStopwatchTimeAndLapUseCaseImplTest {
    @Test
    fun update_shouldCreateFirstLapIfLapsAreEmpty() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 0L,
                laps = emptyList()
            )
        )
        val useCase = UpdateStopwatchTimeAndLapUseCaseImpl(store)

        useCase.execute(
            timerState = TimerState(
                isRunning = true,
                milliseconds = 1_000L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 1_000L,
            laps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = LapStatus.CURRENT
                )
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }

    @Test
    fun update_shouldUpdateFirstLapIfThereIsOnlyOne() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 1_000L,
                laps = listOf(
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = LapStatus.CURRENT
                    )
                )
            )
        )
        val useCase = UpdateStopwatchTimeAndLapUseCaseImpl(store)

        useCase.execute(
            timerState = TimerState(
                isRunning = true,
                milliseconds = 2_000L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 2_000L,
            laps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 2_000L,
                    status = LapStatus.CURRENT
                )
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }

    @Test
    fun update_shouldNotClassifyBestWhenThereIsOnlyTwoLaps() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 1_500L,
                laps = listOf(
                    Lap(
                        index = 2,
                        milliseconds = 500L,
                        status = LapStatus.CURRENT
                    ),
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = LapStatus.DONE
                    )
                )
            )
        )
        val useCase = UpdateStopwatchTimeAndLapUseCaseImpl(store)

        useCase.execute(
            timerState = TimerState(
                isRunning = true,
                milliseconds = 1_700L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 1_700L,
            laps = listOf(
                Lap(
                    index = 2,
                    milliseconds = 700L,
                    status = LapStatus.CURRENT
                ),
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = LapStatus.DONE
                )
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }

    @Test
    fun update_shouldNotClassifyWorstWhenThereIsOnlyTwoLaps() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 2_500L,
                laps = listOf(
                    Lap(
                        index = 2,
                        milliseconds = 1_500L,
                        status = LapStatus.CURRENT
                    ),
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = LapStatus.DONE
                    )
                )
            )
        )
        val useCase = UpdateStopwatchTimeAndLapUseCaseImpl(store)

        useCase.execute(
            timerState = TimerState(
                isRunning = true,
                milliseconds = 2_700L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 2_700L,
            laps = listOf(
                Lap(
                    index = 2,
                    milliseconds = 1_700L,
                    status = LapStatus.CURRENT
                ),
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = LapStatus.DONE
                )
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }

    @Test
    fun update_shouldCorrectlyClassifyLaps() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 4_100L,
                laps = listOf(
                    Lap(
                        index = 4,
                        milliseconds = 1_100L,
                        status = LapStatus.CURRENT
                    ),
                    Lap(
                        index = 3,
                        milliseconds = 1_200L,
                        status = LapStatus.CURRENT
                    ),
                    Lap(
                        index = 2,
                        milliseconds = 800L,
                        status = LapStatus.CURRENT
                    ),
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = LapStatus.CURRENT
                    )
                )
            )
        )
        val useCase = UpdateStopwatchTimeAndLapUseCaseImpl(store)

        useCase.execute(
            timerState = TimerState(
                isRunning = true,
                milliseconds = 4_100L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 4_100L,
            laps = listOf(
                Lap(
                    index = 4,
                    milliseconds = 1_100L,
                    status = LapStatus.CURRENT
                ),
                Lap(
                    index = 3,
                    milliseconds = 1_200L,
                    status = LapStatus.WORST
                ),
                Lap(
                    index = 2,
                    milliseconds = 800L,
                    status = LapStatus.BEST
                ),
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = LapStatus.DONE
                )
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }
}