package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.use_cases.UpdateStopwatchTimeUseCase
import com.rafaeltmbr.stopwatch.domain.utils.CalculateLapsStatuses

class UpdateStopwatchTimeUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
    private val calculateLapsStatuses: CalculateLapsStatuses
) : UpdateStopwatchTimeUseCase {
    override suspend fun execute(timerState: TimerService.State) {
        store.update {
            it.copy(
                milliseconds = timerState.milliseconds,
                laps = getNextLaps(timerState.milliseconds, it.laps)
            )
        }
    }

    private fun getNextLaps(milliseconds: Long, laps: List<Lap>): List<Lap> {
        if (laps.size < 2) {
            return listOf(
                Lap(
                    index = 1,
                    milliseconds = milliseconds,
                    status = Lap.Status.CURRENT
                )
            )
        }

        val currentLapTime = milliseconds - laps.subList(0, laps.size - 1).sumOf { it.milliseconds }
        val lapsWithTimeUpdated =
            laps.map { if (it == laps.last()) it.copy(milliseconds = currentLapTime) else it }
        return calculateLapsStatuses.execute(lapsWithTimeUpdated)
    }
}