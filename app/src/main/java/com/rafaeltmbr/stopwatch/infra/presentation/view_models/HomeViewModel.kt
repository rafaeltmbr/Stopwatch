package com.rafaeltmbr.stopwatch.infra.presentation.view_models

import com.rafaeltmbr.stopwatch.domain.entities.StopwatchStatus
import kotlinx.coroutines.flow.StateFlow

data class HomeViewTime(
    val minutes: List<String>,
    val seconds: List<String>,
    val fraction: List<String>
)

data class HomeViewLap(
    val index: Int,
    val time: String,
    val diff: String
)

data class HomeViewState(
    val status: StopwatchStatus,
    val time: HomeViewTime,
    val laps: List<HomeViewLap>,
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
