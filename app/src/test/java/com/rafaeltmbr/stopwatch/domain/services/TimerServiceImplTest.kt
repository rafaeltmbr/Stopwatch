package com.rafaeltmbr.stopwatch.domain.services

import com.rafaeltmbr.stopwatch.domain.services.impl.TimerServiceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimerServiceImplTest {
    @Test
    fun state_shouldHaveAValidInitialState() {
        val service = TimerServiceImpl()
        val expected = TimerState(
            isRunning = false,
            milliseconds = 0L
        )

        Assert.assertEquals(expected, service.state.value)
    }

    @Test
    fun start_shouldStartTimer() = runTest {
        val service = TimerServiceImpl(coroutineScope = this)
        service.start()
        Assert.assertTrue(service.state.value.isRunning)

        delay(1_000L)
        service.pause()
        runCurrent()
        Assert.assertTrue(service.state.value.milliseconds > 1L)
    }

    @Test
    fun start_shouldPauseTimer() = runTest {
        val service = TimerServiceImpl(coroutineScope = this)

        service.start()
        delay(1_000L)
        service.pause()
        runCurrent()

        Assert.assertFalse(service.state.value.isRunning)
        Assert.assertTrue(service.state.value.milliseconds > 1L)
    }

    @Test
    fun start_shouldResetTimer() = runTest {
        val service = TimerServiceImpl(coroutineScope = this)

        service.start()
        delay(1_000L)
        service.pause()
        service.reset()
        runCurrent()

        Assert.assertFalse(service.state.value.isRunning)
        Assert.assertEquals(0L, service.state.value.milliseconds)
    }

    @Test
    fun start_shouldNotResetWhileRunning() = runTest {
        val service = TimerServiceImpl(coroutineScope = this)

        service.start()
        delay(1_000L)
        service.reset()
        Assert.assertTrue(service.state.value.isRunning)

        service.pause()
        runCurrent()
        Assert.assertTrue(service.state.value.milliseconds > 1L)
    }
}