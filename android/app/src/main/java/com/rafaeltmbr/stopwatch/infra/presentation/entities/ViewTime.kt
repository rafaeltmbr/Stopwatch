package com.rafaeltmbr.stopwatch.infra.presentation.entities

data class ViewTime(
    val minutes: List<String>,
    val seconds: List<String>,
    val fraction: List<String>
)