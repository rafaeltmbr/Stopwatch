package com.rafaeltmbr.stopwatch.core.entities

class StopwatchState(
    status: Status = Status.INITIAL,
    time: Time = Time(),
    completedLaps: CompletedLaps = CompletedLaps()
) {
    var status: Status
        private set

    var time: Time
        private set

    var completedLaps: CompletedLaps
        private set

    init {
        this.status = status
        this.time = time
        this.completedLaps = completedLaps
    }

    fun copy(): StopwatchState {
        return StopwatchState(
            status = status,
            time = time.copy(),
            completedLaps = completedLaps.copy(),
        )
    }

    fun reset(): StopwatchState {
        status = Status.INITIAL
        time = Time()
        completedLaps = CompletedLaps()
        return this
    }

    fun pause(): StopwatchState {
        status = Status.PAUSED
        return this
    }

    fun start(): StopwatchState {
        status = Status.RUNNING
        return this
    }

    fun updateTime(time: Time): StopwatchState {
        this.time = time.copy()
        return this
    }

    fun lap(): StopwatchState {
        completedLaps.addNew(Time(time.milliseconds))
        return this
    }

    override fun equals(other: Any?): Boolean {
        return other is StopwatchState
                && status == other.status
                && time == other.time
                && completedLaps == other.completedLaps
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "StopwatchState(status=$status, time=$time, completedLaps=$completedLaps)"
    }
}
