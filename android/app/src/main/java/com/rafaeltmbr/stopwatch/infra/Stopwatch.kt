package com.rafaeltmbr.stopwatch.infra

import android.app.Application
import com.rafaeltmbr.stopwatch.infra.di.ApplicationContainer
import com.rafaeltmbr.stopwatch.infra.di.impl.ApplicationApplicationContainerFactoryImpl
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
    }

    private fun restoreStopwatchState() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                container.services.logging.debug(TAG, "Restoring stopwatch state...")
                container.useCases.restoreStopwatchState.execute()
                container.services.logging.debug(TAG, "Stopwatch state restored")
                
                container.services.timer.state.collect(
                    container.useCases.updateStopwatchTimeAndLap::execute
                )
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                container.services.logging.error(TAG, "Failed to restore stopwatch state", e)
            }
        }
    }
}