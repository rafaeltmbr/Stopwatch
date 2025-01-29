package com.rafaeltmbr.stopwatch.platform.presentation.view_models

import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewTime
import kotlinx.coroutines.flow.StateFlow


interface HomeViewModel {
    data class State(
        val status: Status = Status.INITIAL,
        val time: ViewTime = ViewTime(
            minutes = listOf("0", "0"),
            seconds = listOf("0", "0"),
            fraction = listOf("0", "0")
        ),
        val laps: List<ViewLap> = emptyList(),
        val showLapsSection: Boolean = false,
        val showSeeAllLaps: Boolean = false
    )

    sealed class Action {
        data object Start : Action()
        data object Pause : Action()
        data object Resume : Action()
        data object Reset : Action()
        data object Lap : Action()
        data object SeeAll : Action()
    }

    val state: StateFlow<State>
    fun handleAction(action: Action)
}
