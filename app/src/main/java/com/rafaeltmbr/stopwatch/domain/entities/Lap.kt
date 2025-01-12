package com.rafaeltmbr.stopwatch.domain.entities

data class Lap(
    val index: Int,
    val time: Long, // ms
    val diff: Long // ms
)
