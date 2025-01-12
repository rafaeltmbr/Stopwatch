package com.rafaeltmbr.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import com.rafaeltmbr.stopwatch.infra.di.impl.ViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.theme.StopwatchTheme
import com.rafaeltmbr.stopwatch.infra.presentation.views.HomeView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModelFactory = ViewModelFactory(
                coroutineScope = rememberCoroutineScope()
            )
            
            StopwatchTheme {
                HomeView(homeViewModelFactory = viewModelFactory)
            }
        }
    }
}
