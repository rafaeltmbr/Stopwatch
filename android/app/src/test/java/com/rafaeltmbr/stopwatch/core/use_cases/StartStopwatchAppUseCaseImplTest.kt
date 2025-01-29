package com.rafaeltmbr.stopwatch.core.use_cases

import com.rafaeltmbr.stopwatch.core.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.StartStopwatchUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class StartStopwatchAppUseCaseImplTest {
    @Test
    fun start_shouldExecuteTimerAndUpdateStateStore() = runTest {
        val store = MutableStateStoreImpl(StopwatchState())
        val service = TimerServiceImpl()
        val useCase = StartStopwatchUseCaseImpl(store, service)

        useCase.execute()
        Assert.assertEquals(Status.RUNNING, store.state.value.status)
        Assert.assertTrue(service.state.value.isRunning)
    }

    @Test
    fun start_shouldIgnoreStartWhenStopwatchIsAlreadyRunning() = runTest {
        val initial = StopwatchState(
            status = Status.INITIAL,
            milliseconds = 0L,
            completedLaps = emptyList()
        )

        val store = MutableStateStoreImpl(initial)
        val service = TimerServiceImpl()
        val useCase = StartStopwatchUseCaseImpl(store, service)

        useCase.execute()
        useCase.execute()
        Assert.assertEquals(Status.RUNNING, store.state.value.status)
        Assert.assertTrue(service.state.value.isRunning)
    }
}