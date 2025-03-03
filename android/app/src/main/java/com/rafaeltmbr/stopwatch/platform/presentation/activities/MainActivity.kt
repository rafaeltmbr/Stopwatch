package com.rafaeltmbr.stopwatch.platform.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rafaeltmbr.stopwatch.platform.StopwatchApp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val container = (applicationContext as StopwatchApp).container
            container.presentation.stackNavigator.NavigationStack(
                container.presentation.homeViewModelFactory,
                container.presentation.lapsViewModelFactory
            )
        }
    }

    override fun onPause() {
        super.onPause()

        CoroutineScope(Dispatchers.IO).launch {
            val container = (applicationContext as StopwatchApp).container
            try {
                container.services.logging.debug(TAG, "Saving stopwatch state...")
                container.useCases.saveStopwatchState.execute()
                container.services.logging.debug(TAG, "Stopwatch state saved")
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                container.services.logging.error(TAG, "Failed to save stopwatch state", e)
            }
        }
    }
}