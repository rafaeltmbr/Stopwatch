package com.rafaeltmbr.stopwatch.domain.use_cases

import com.rafaeltmbr.stopwatch.domain.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.UpdateStopwatchTimeUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.utils.impl.CalculateLapsStatusesImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UpdateStopwatchTimeUseCaseImplTest {
    @Test
    fun update_shouldCreateFirstLapIfLapsAreEmpty() = runTest {
        val store = MutableStateStoreImpl(
            StopwatchState(
                status = Status.RUNNING,
                milliseconds = 0L,
                completedLaps = emptyList()
            )
        )
        val useCase = UpdateStopwatchTimeUseCaseImpl(store, CalculateLapsStatusesImpl())

        useCase.execute(
            timerState = TimerService.State(
                isRunning = true,
                milliseconds = 1_000L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 1_000L,
            completedLaps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = Lap.Status.CURRENT
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
                completedLaps = listOf(
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = Lap.Status.CURRENT
                    )
                )
            )
        )
        val useCase = UpdateStopwatchTimeUseCaseImpl(store, CalculateLapsStatusesImpl())

        useCase.execute(
            timerState = TimerService.State(
                isRunning = true,
                milliseconds = 2_000L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 2_000L,
            completedLaps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 2_000L,
                    status = Lap.Status.CURRENT
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
                completedLaps = listOf(
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = Lap.Status.DONE
                    ),
                    Lap(
                        index = 2,
                        milliseconds = 500L,
                        status = Lap.Status.CURRENT
                    ),
                )
            )
        )
        val useCase = UpdateStopwatchTimeUseCaseImpl(store, CalculateLapsStatusesImpl())

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
                ),
                Lap(
                    index = 2,
                    milliseconds = 700L,
                    status = Lap.Status.CURRENT
                ),
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
                completedLaps = listOf(
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = Lap.Status.DONE
                    ),
                    Lap(
                        index = 2,
                        milliseconds = 1_500L,
                        status = Lap.Status.CURRENT
                    ),
                )
            )
        )
        val useCase = UpdateStopwatchTimeUseCaseImpl(store, CalculateLapsStatusesImpl())

        useCase.execute(
            timerState = TimerService.State(
                isRunning = true,
                milliseconds = 2_700L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 2_700L,
            completedLaps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = Lap.Status.DONE
                ),
                Lap(
                    index = 2,
                    milliseconds = 1_700L,
                    status = Lap.Status.CURRENT
                ),
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
                completedLaps = listOf(
                    Lap(
                        index = 1,
                        milliseconds = 1_000L,
                        status = Lap.Status.CURRENT
                    ),
                    Lap(
                        index = 2,
                        milliseconds = 800L,
                        status = Lap.Status.CURRENT
                    ),
                    Lap(
                        index = 3,
                        milliseconds = 1_200L,
                        status = Lap.Status.CURRENT
                    ),
                    Lap(
                        index = 4,
                        milliseconds = 1_100L,
                        status = Lap.Status.CURRENT
                    ),
                )
            )
        )
        val useCase = UpdateStopwatchTimeUseCaseImpl(store, CalculateLapsStatusesImpl())

        useCase.execute(
            timerState = TimerService.State(
                isRunning = true,
                milliseconds = 4_100L
            )
        )

        val expected = StopwatchState(
            status = Status.RUNNING,
            milliseconds = 4_100L,
            completedLaps = listOf(
                Lap(
                    index = 1,
                    milliseconds = 1_000L,
                    status = Lap.Status.DONE
                ),
                Lap(
                    index = 2,
                    milliseconds = 800L,
                    status = Lap.Status.BEST
                ),
                Lap(
                    index = 3,
                    milliseconds = 1_200L,
                    status = Lap.Status.WORST
                ),
                Lap(
                    index = 4,
                    milliseconds = 1_100L,
                    status = Lap.Status.CURRENT
                ),
            )
        )

        Assert.assertEquals(expected, store.state.value)
    }
}