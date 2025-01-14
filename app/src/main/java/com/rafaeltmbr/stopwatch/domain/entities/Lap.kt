package com.rafaeltmbr.stopwatch.domain.entities


data class Lap(
    val index: Int,
    val milliseconds: Long,
    val status: Status
) {
    enum class Status {
        CURRENT,
        BEST,
        WORST,
        DONE
    }
}
