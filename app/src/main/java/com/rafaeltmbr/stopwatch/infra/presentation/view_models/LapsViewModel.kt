package com.rafaeltmbr.stopwatch.infra.presentation.view_models

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import kotlinx.coroutines.flow.StateFlow


interface LapsViewModel {
    data class State(
        val status: Status = Status.INITIAL,
        val time: String = "00:00.00",
        val laps: List<ViewLap> = emptyList(),
    )

    sealed class Action {
        data object Resume : Action()
        data object Lap : Action()
        data object NavigateBack : Action()
    }

    val state: StateFlow<State>
    fun handleAction(action: Action)
}