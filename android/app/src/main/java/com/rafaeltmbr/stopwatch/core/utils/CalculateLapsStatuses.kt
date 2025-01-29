package com.rafaeltmbr.stopwatch.core.utils

import com.rafaeltmbr.stopwatch.core.entities.Lap

interface CalculateLapsStatuses {
    fun execute(laps: List<Lap>): List<Lap>
}