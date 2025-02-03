package com.rafaeltmbr.stopwatch.core.services

import com.rafaeltmbr.stopwatch.core.services.impl.TimerServiceImpl
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
        val expected = TimerService.State(
            isRunning = false,
            milliseconds = 0L
        )

        Assert.assertEquals(expected, service.state.value)
    }

    @Test
    fun start_shouldStartTimer() = runTest {
        val service = TimerServiceImpl(
            coroutineScope = this,
            currentMillisecondsCallback = testScheduler::currentTime
        )
        service.start()
        Assert.assertTrue(service.state.value.isRunning)

        delay(1_050L)
        service.pause()
        runCurrent()
        Assert.assertTrue(service.state.value.milliseconds > 1_000L)
    }

    @Test
    fun pause_shouldPauseTimer() = runTest {
        val service = TimerServiceImpl(
            coroutineScope = this,
            currentMillisecondsCallback = testScheduler::currentTime
        )

        service.start()
        delay(1_050L)
        service.pause()
        runCurrent()

        Assert.assertFalse(service.state.value.isRunning)
        Assert.assertTrue(service.state.value.milliseconds > 1_000L)
    }

    @Test
    fun reset_shouldResetTimer() = runTest {
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
    fun reset_shouldNotResetWhileRunning() = runTest {
        val service = TimerServiceImpl(
            coroutineScope = this,
            currentMillisecondsCallback = testScheduler::currentTime
        )

        service.start()
        delay(1_050L)
        service.reset()
        Assert.assertTrue(service.state.value.isRunning)

        service.pause()
        runCurrent()
        Assert.assertTrue(service.state.value.milliseconds > 1_000L)
    }
}