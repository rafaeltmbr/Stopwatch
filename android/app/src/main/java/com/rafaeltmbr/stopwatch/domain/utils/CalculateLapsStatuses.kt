package com.rafaeltmbr.stopwatch.domain.utils

import com.rafaeltmbr.stopwatch.domain.entities.Lap

interface CalculateLapsStatuses {
    fun execute(laps: List<Lap>): List<Lap>
}