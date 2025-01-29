package com.rafaeltmbr.stopwatch.platform.presentation.navigation

import kotlinx.coroutines.flow.StateFlow

interface StackNavigator {
    sealed class Screen(val name: String) {
        data object Home : Screen("Home")
        data object Laps : Screen("Laps")
    }

    val stack: StateFlow<List<Screen>>
    suspend fun push(screen: Screen)
    suspend fun pop()
}