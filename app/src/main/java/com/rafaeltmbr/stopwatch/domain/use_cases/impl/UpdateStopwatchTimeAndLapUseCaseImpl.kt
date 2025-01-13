package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.LapStatus
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.services.TimerState
import com.rafaeltmbr.stopwatch.domain.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.domain.use_cases.UpdateStopwatchTimeAndLapUseCase

class UpdateStopwatchTimeAndLapUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>
) : UpdateStopwatchTimeAndLapUseCase {
    override suspend fun execute(timerState: TimerState) {
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
                    status = LapStatus.CURRENT
                )
            )
        }

        val currentLapTime = milliseconds - laps.subList(1, laps.size).sumOf { it.milliseconds }
        val lapsWithTimeUpdated =
            laps.map { if (it == laps.first()) it.copy(milliseconds = currentLapTime) else it }
        return updateLapsStatuses(lapsWithTimeUpdated)
    }

    companion object {
        fun updateLapsStatuses(laps: List<Lap>): List<Lap> {
            if (laps.size < 3) {
                return laps.map {
                    it.copy(
                        status = if (it == laps.first()) LapStatus.CURRENT else LapStatus.DONE
                    )
                }
            }

            val current = laps.first()
            var best = laps[1]
            var worst = laps[1]

            for (lap in laps.subList(1, laps.size)) {
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
                        current -> LapStatus.CURRENT
                        best -> LapStatus.BEST
                        worst -> LapStatus.WORST
                        else -> LapStatus.DONE
                    }
                )
            }
        }
    }
}