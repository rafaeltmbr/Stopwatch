package com.rafaeltmbr.stopwatch.infra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val container = (applicationContext as Stopwatch).container
            container.stackNavigator.NavigationStack(
                container.homeViewModelFactory,
                container.lapsViewModelFactory
            )
        }
    }
}
