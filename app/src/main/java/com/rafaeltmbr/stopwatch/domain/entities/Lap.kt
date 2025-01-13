package com.rafaeltmbr.stopwatch.domain.entities

enum class LapStatus {
    CURRENT,
    BEST,
    WORST,
    DONE
}

data class Lap(
    val index: Int,
    val milliseconds: Long,
    val status: LapStatus
)
