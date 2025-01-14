package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.LapStatus
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase

class NewLapUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
) : NewLapUseCase {
    override suspend fun execute() {
        store.update {
            val newLap = Lap(
                index = it.laps.size + 1,
                milliseconds = 0L,
                status = LapStatus.CURRENT
            )

            val mergedLaps = it.laps + newLap

            it.copy(laps = UpdateStopwatchTimeAndLapUseCaseImpl.updateLapsStatuses(mergedLaps))
        }
    }
}