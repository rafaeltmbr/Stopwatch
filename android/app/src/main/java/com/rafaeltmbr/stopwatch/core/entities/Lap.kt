package com.rafaeltmbr.stopwatch.core.entities


data class Lap(
    val index: Int,
    val milliseconds: Long = 0L,
    val status: Status = Status.CURRENT
) {
    enum class Status {
        CURRENT,
        BEST,
        WORST,
        DONE
    }
}
