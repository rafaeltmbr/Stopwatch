package com.rafaeltmbr.stopwatch.infra.di

import androidx.compose.runtime.Composable
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewModel

interface HomeViewModelFactory {
    @Composable
    fun makeHomeViewModel(): HomeViewModel
}