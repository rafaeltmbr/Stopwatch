package com.rafaeltmbr.stopwatch.domain.entities

data class Lap(
    val index: Int,
    val time: Float,
    val diff: Float
)
