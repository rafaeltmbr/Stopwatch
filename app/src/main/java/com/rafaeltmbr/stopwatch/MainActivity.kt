package com.rafaeltmbr.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rafaeltmbr.stopwatch.presentation.theme.StopwatchTheme
import com.rafaeltmbr.stopwatch.presentation.views.HomeView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopwatchTheme {
                HomeView()
            }
        }
    }
}