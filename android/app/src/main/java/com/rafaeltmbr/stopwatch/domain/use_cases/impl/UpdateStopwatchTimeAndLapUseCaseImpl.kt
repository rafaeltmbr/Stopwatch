package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerService
import com.rafaeltmbr.stopwatch.domain.use_cases.UpdateStopwatchTimeAndLapUseCase

class UpdateStopwatchTimeAndLapUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>
) : UpdateStopwatchTimeAndLapUseCase {
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
        return updateLapsStatuses(lapsWithTimeUpdated)
    }

    companion object {
        fun updateLapsStatuses(laps: List<Lap>): List<Lap> {
            if (laps.size < 3) {
                return laps.map {
                    it.copy(
                        status = if (it == laps.last()) Lap.Status.CURRENT else Lap.Status.DONE
                    )
                }
            }

            val current = laps.last()
            var best = laps[0]
            var worst = laps[0]

            for (lap in laps.subList(0, laps.size - 1)) {
                if (lap.milliseconds < best.milliseconds) {
                    best = lap
                } else if (lap.milliseconds > worst.milliseconds && laps.size > 2) {
                    worst = lap
                }
            }

            return laps.map {
                Lap(
                    index = it.index,
                    milliseconds = it.milliseconds,
                    status = when (it) {
                        current -> Lap.Status.CURRENT
                        best -> Lap.Status.BEST
                        worst -> Lap.Status.WORST
                        else -> Lap.Status.DONE
                    }
                )
            }
        }
    }
}