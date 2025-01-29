package com.rafaeltmbr.stopwatch.platform

import android.app.Application
import com.rafaeltmbr.stopwatch.platform.di.ApplicationContainer
import com.rafaeltmbr.stopwatch.platform.di.impl.ApplicationApplicationContainerFactoryImpl
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "Stopwatch"

class Stopwatch : Application() {

    lateinit var container: ApplicationContainer

    override fun onCreate() {
        super.onCreate()

        container = ApplicationApplicationContainerFactoryImpl(this).make()
        restoreStopwatchState()
        handleTimerUpdate()
    }

    private fun restoreStopwatchState() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                container.services.logging.debug(TAG, "Restoring stopwatch state...")
                container.useCases.restoreStopwatchState.execute()
                container.services.logging.debug(TAG, "Stopwatch state restored")
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                container.services.logging.error(TAG, "Failed to restore stopwatch state", e)
            }
        }
    }

    private fun handleTimerUpdate() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                container.services.timer.state.collect(
                    container.useCases.updateStopwatchTime::execute
                )
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                container.services.logging.error(TAG, "Failed handle timer update", e)
            }
        }
    }
}