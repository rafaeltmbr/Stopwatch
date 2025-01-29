package com.rafaeltmbr.stopwatch.platform.di

import androidx.compose.runtime.Composable
import com.rafaeltmbr.stopwatch.platform.presentation.view_models.LapsViewModel

interface LapsViewModelFactory {
    @Composable
    fun make(): LapsViewModel
}