package com.rafaeltmbr.stopwatch.core.entities

class Time(
    milliseconds: Long = 0L
) {
    var milliseconds: Long
        private set

    init {
        this.milliseconds = milliseconds
        validateMilliseconds()
    }

    fun copy(): Time {
        return Time(milliseconds)
    }

    private fun validateMilliseconds() {
        if (milliseconds < 0L) {
            throw Error("Invalid time. Time should be positive or zero.")
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Time && milliseconds == other.milliseconds
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Time(milliseconds=$milliseconds)"
    }
}