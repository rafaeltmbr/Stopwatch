package com.rafaeltmbr.stopwatch.infra.presentation.view_models

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import kotlinx.coroutines.flow.StateFlow

data class LapsViewState(
    val status: Status = Status.INITIAL,
    val time: String = "00:00.00",
    val laps: List<ViewLap> = emptyList(),
)

sealed class LapsViewAction {
    data object Resume : LapsViewAction()
    data object Lap : LapsViewAction()
    data object NavigateBack : LapsViewAction()
}

interface LapsViewModel {
    val state: StateFlow<LapsViewState>
    fun handleAction(action: LapsViewAction)
}