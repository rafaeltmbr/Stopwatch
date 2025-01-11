package com.rafaeltmbr.stopwatch.infra.presentation.view_models

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime
import kotlinx.coroutines.flow.StateFlow


data class HomeViewState(
    val status: Status,
    val time: ViewTime,
    val laps: List<ViewLap>,
    val showSeeMore: Boolean
)

sealed class HomeViewAction {
    data object Start : HomeViewAction()
    data object Pause : HomeViewAction()
    data object Resume : HomeViewAction()
    data object Reset : HomeViewAction()
    data object Lap : HomeViewAction()
    data object SeeAll : HomeViewAction()
}

interface HomeViewModel {
    val state: StateFlow<HomeViewState>
    fun handleAction(action: HomeViewAction)
}
