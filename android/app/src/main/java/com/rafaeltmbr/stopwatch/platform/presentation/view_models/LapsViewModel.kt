package com.rafaeltmbr.stopwatch.platform.presentation.view_models

import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewLap
import kotlinx.coroutines.flow.StateFlow


interface LapsViewModel {
    data class State(
        val status: Status = Status.INITIAL,
        val time: String = "00:00.00",
        val lapsCount: Int = 0
    )

    sealed class Action {
        data object Resume : Action()
        data object Lap : Action()
        data object NavigateBack : Action()
    }

    val state: StateFlow<State>
    fun handleAction(action: Action)
    fun getViewLapByReversedArrayIndex(index: Int): ViewLap
}