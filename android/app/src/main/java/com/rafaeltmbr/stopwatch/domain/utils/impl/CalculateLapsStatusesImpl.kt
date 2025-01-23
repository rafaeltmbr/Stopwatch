package com.rafaeltmbr.stopwatch.domain.utils.impl

import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.utils.CalculateLapsStatuses

class CalculateLapsStatusesImpl : CalculateLapsStatuses {
    override fun execute(laps: List<Lap>): List<Lap> {
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