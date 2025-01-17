package com.rafaeltmbr.stopwatch.infra.presentation.entities

sealed class Screen(val name: String) {
    data object Home : Screen("Home")
    data object Laps : Screen("Laps")
}