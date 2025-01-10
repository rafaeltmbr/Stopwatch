package com.rafaeltmbr.stopwatch.infra.di.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafaeltmbr.stopwatch.infra.di.ViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.HomeViewModelImpl

class ViewModelFactoryImpl : ViewModelFactory {
    @Composable
    override fun makeHomeViewModel(): HomeViewModel = viewModel<HomeViewModelImpl>()
}