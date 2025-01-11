package com.rafaeltmbr.stopwatch.domain.use_cases

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class StartStopwatchUseCaseImplTest {
    @Test
    fun start_shouldChangeStoreStateAfterExecute() = runTest {
        val initial = StopwatchState(
            status = Status.INITIAL,
            milliseconds = 0L,
            laps = emptyList()
        )

        val store = MutableStateStoreImpl(initial.copy())
        val service = TimerServiceImpl()
        StartStopwatchUseCaseImpl(store, service)

        Assert.assertEquals(initial, store.state.value)
    }

    @Test
    fun start_shouldExecuteTimerAndUpdateStateStore() = runTest {
        val initial = StopwatchState(
            status = Status.INITIAL,
            milliseconds = 0L,
            laps = emptyList()
        )

        val store = MutableStateStoreImpl(initial)
        val service = TimerServiceImpl()
        val useCase = StartStopwatchUseCaseImpl(store, service)

        Assert.assertEquals(initial, store.state.value)
        Assert.assertFalse(service.state.value.isRunning)

        useCase.execute()

        Assert.assertEquals(Status.RUNNING, store.state.value.status)
        Assert.assertTrue(service.state.value.isRunning)
    }
}