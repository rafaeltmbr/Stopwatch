package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.use_cases.NewLapUseCase
import com.rafaeltmbr.stopwatch.domain.utils.CalculateLapsStatuses

class NewLapUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val calculateLapsStatuses: CalculateLapsStatuses
) : NewLapUseCase {
    override suspend fun execute() {
        store.update {
            val newLap = Lap(
                index = it.laps.size + 1,
                milliseconds = 0L,
                status = Lap.Status.CURRENT
            )

            val mergedLaps = it.laps + newLap

            it.copy(laps = calculateLapsStatuses.execute(mergedLaps))
        }
    }
}