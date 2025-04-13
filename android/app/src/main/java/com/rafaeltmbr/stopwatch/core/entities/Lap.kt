package com.rafaeltmbr.stopwatch.core.entities


data class Lap(
    val index: Int,
    val time: Time = Time(),
    val status: Status = Status.CURRENT,
) {
    enum class Status {
        CURRENT,
        BEST,
        WORST,
        DONE
    }
}
