package com.rafaeltmbr.stopwatch.platform.di

import androidx.compose.runtime.Composable
import com.rafaeltmbr.stopwatch.platform.presentation.view_models.HomeViewModel

interface HomeViewModelFactory {
    @Composable
    fun make(): HomeViewModel
}