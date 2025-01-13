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

        var best = laps.first()
        var worst = laps.first()
        val current = laps.first()
        val currentLapTime = milliseconds - laps.subList(1, laps.size).sumOf { it.milliseconds }

        for (lap in laps) {
            val lapTime = when (lap) {
                current -> currentLapTime
                else -> lap.milliseconds
            }

            if (lapTime < best.milliseconds && lap != current) {
                best = lap
            } else if (lapTime > worst.milliseconds && laps.size > 2) {
                worst = lap
            }
        }

        return laps.map {
            Lap(
                index = it.index,
                milliseconds = when (it) {
                    current -> currentLapTime
                    else -> it.milliseconds
                },
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