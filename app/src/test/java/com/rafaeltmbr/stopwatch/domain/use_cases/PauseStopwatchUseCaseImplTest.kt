package com.rafaeltmbr.stopwatch.domain.use_cases

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import com.rafaeltmbr.stopwatch.domain.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.PauseStopwatchUseCaseImpl
import com.rafaeltmbr.stopwatch.domain.use_cases.impl.StartStopwatchUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PauseStopwatchUseCaseImplTest {
    @Test
    fun pause_shouldStopTimerAndUpdateStateStore() = runTest {
        val store = MutableStateStoreImpl(StopwatchState())
        val service = TimerServiceImpl()
        val startStopwatch = StartStopwatchUseCaseImpl(store, service)
        val pauseStopwatch = PauseStopwatchUseCaseImpl(store, service)

        startStopwatch.execute()
        Assert.assertEquals(Status.RUNNING, store.state.value.status)
        Assert.assertTrue(service.state.value.isRunning)

        pauseStopwatch.execute()
        Assert.assertEquals(Status.PAUSED, store.state.value.status)
        Assert.assertFalse(service.state.value.isRunning)
    }

    @Test
    fun pause_shouldNotPauseWhenStopwatchIsNotRunning() = runTest {
        val store = MutableStateStoreImpl(StopwatchState())
        val service = TimerServiceImpl()
        val pauseStopwatch = PauseStopwatchUseCaseImpl(store, service)

        pauseStopwatch.execute()
        Assert.assertEquals(Status.INITIAL, store.state.value.status)
        Assert.assertFalse(service.state.value.isRunning)
    }
}