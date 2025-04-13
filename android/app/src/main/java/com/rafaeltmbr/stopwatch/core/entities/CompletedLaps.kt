package com.rafaeltmbr.stopwatch.core.entities

class CompletedLaps(
    laps: List<Lap> = emptyList(),
    time: Time = Time()
) {
    var laps: List<Lap>
        private set

    var time: Time
        private set

    init {
        this.laps = laps
        this.time = time
    }

    fun addNew(time: Time) {
        val newLap = Lap(
            index = laps.size + 1,
            time = Time(milliseconds = time.milliseconds - this.time.milliseconds),
            status = Lap.Status.DONE
        )

        val mergedLaps = laps + newLap

        laps = updateStatuses(mergedLaps)
        this.time = time
    }

    fun copy(): CompletedLaps {
        return CompletedLaps(laps.map { it }, time.copy())

    }

    private fun updateStatuses(laps: List<Lap>): List<Lap> {
        if (laps.size < 2) {
            return laps.map { it.copy(status = Lap.Status.DONE) }
        }

        var best = laps[0]
        var worst = laps[0]

        for (lap in laps) {
            if (lap.time.milliseconds < best.time.milliseconds) {
                best = lap
            } else if (lap.time.milliseconds > worst.time.milliseconds) {
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

    override fun equals(other: Any?): Boolean {
        return other is CompletedLaps
                && laps == other.laps
                && time == other.time
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "CompletedLaps(laps=$laps, time=$time)"
    }
}