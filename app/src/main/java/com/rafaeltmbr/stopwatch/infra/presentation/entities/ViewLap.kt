package com.rafaeltmbr.stopwatch.infra.presentation.entities

data class ViewLap(
    val index: Int,
    val time: String,
    val diff: String
)
