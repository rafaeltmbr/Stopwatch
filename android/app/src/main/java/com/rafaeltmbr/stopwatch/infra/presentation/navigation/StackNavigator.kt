package com.rafaeltmbr.stopwatch.infra.presentation.navigation

import com.rafaeltmbr.stopwatch.infra.presentation.entities.Screen

interface StackNavigator {
    suspend fun push(screen: Screen)
    suspend fun pop()
}