package com.rafaeltmbr.stopwatch.domain.utils.impl

import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.utils.CalculateLapsStatuses

class CalculateLapsStatusesImpl : CalculateLapsStatuses {
    override fun execute(laps: List<Lap>): List<Lap> {
        if (laps.size < 2) {
            return laps.map { it.copy(status = Lap.Status.DONE) }
        }

        var best = laps[0]
        var worst = laps[0]

        for (lap in laps) {
            if (lap.milliseconds < best.milliseconds) {
                best = lap
            } else if (lap.milliseconds > worst.milliseconds) {
                worst = lap
            }
        }

        return laps.map {
            it.copy(
                status = when (it) {
                    best -> Lap.Status.BEST
                    worst -> Lap.Status.WORST
                    else -> Lap.Status.DONE
                }
            )
        }
    }
}