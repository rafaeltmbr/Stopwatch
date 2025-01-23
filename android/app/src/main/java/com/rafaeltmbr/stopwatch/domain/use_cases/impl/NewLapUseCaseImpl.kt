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
                index = it.completedLaps.size + 1,
                milliseconds = it.milliseconds - it.completedLapsMilliseconds,
                status = Lap.Status.DONE
            )

            val mergedLaps = it.completedLaps + newLap

            it.copy(
                completedLaps = calculateLapsStatuses.execute(mergedLaps),
                completedLapsMilliseconds = it.milliseconds
            )
        }
    }
}