package com.rafaeltmbr.stopwatch.infra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
            val container = (applicationContext as Stopwatch).container
            container.presentation.stackNavigator.NavigationStack(
                container.presentation.homeViewModelFactory,
                container.presentation.lapsViewModelFactory
            )
        }
    }

    override fun onPause() {
        super.onPause()

        CoroutineScope(Dispatchers.IO).launch {
            val container = (applicationContext as Stopwatch).container
            try {
                container.useCases.saveStopwatchState.execute()
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                container.services.logging.error(TAG, "Failed to save stopwatch state", e)
            }
        }
    }
}
