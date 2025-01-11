package com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl

import androidx.lifecycle.ViewModel
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchStatus
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewAction
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewModel
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModelImpl : ViewModel(), HomeViewModel {
    private val _state = MutableStateFlow(
        HomeViewState(
            status = StopwatchStatus.INITIAL,
            time = ViewTime(
                minutes = listOf("0", "0"),
                seconds = listOf("0", "0"),
                fraction = listOf("0", "0")
            ),
            laps = emptyList(),
            showSeeMore = false
        )
    )

    override val state: StateFlow<HomeViewState>
        get() = _state.asStateFlow()

    override fun handleAction(action: HomeViewAction) {
        when (action) {
            HomeViewAction.Start -> TODO("Implement start")
            HomeViewAction.Pause -> TODO("Implement pause")
            HomeViewAction.Resume -> TODO("Implement resume")
            HomeViewAction.Reset -> TODO("Implement reset")
            HomeViewAction.Lap -> TODO("Implement lap")
            HomeViewAction.SeeAll -> TODO("Implement see all")
        }
    }
}