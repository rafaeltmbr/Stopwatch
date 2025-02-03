package com.rafaeltmbr.stopwatch.core.use_cases

import com.rafaeltmbr.stopwatch.core.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.PauseStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.ResetStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.core.use_cases.impl.StartStopwatchUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ResetStopwatchAppUseCaseImplTest {
    @Test
    fun reset_shouldResetTimerAndUpdateStateStore() = runTest {
        val store = MutableStateStoreImpl(StopwatchState())
        val service = TimerServiceImpl()
        val startStopwatch = StartStopwatchUseCaseImpl(store, service)
        val pauseStopwatch = PauseStopwatchUseCaseImpl(store, service)
        val resetStopwatch = ResetStopwatchUseCaseImpl(store, service)

        startStopwatch.execute()
        Assert.assertEquals(Status.RUNNING, store.state.value.status)
        Assert.assertTrue(service.state.value.isRunning)
        pauseStopwatch.execute()

        Assert.assertEquals(Status.PAUSED, store.state.value.status)
        Assert.assertFalse(service.state.value.isRunning)
        resetStopwatch.execute()

        Assert.assertEquals(Status.INITIAL, store.state.value.status)
        Assert.assertFalse(service.state.value.isRunning)
    }

    @Test
    fun reset_shouldNotResetStopwatchIsNotPaused() = runTest {
        val store = MutableStateStoreImpl(StopwatchState())
        val service = TimerServiceImpl()
        val startStopwatch = StartStopwatchUseCaseImpl(store, service)
        val resetStopwatch = ResetStopwatchUseCaseImpl(store, service)

        startStopwatch.execute()
        Assert.assertEquals(Status.RUNNING, store.state.value.status)
        Assert.assertTrue(service.state.value.isRunning)

        resetStopwatch.execute()
        Assert.assertEquals(Status.RUNNING, store.state.value.status)
        Assert.assertTrue(service.state.value.isRunning)
    }
}