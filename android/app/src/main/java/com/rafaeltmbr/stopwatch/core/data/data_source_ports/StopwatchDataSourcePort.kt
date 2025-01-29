package com.rafaeltmbr.stopwatch.core.data.data_source_ports

import com.rafaeltmbr.stopwatch.core.entities.StopwatchState

interface StopwatchDataSourcePort {
    suspend fun load(): StopwatchState?
    suspend fun save(state: StopwatchState)
}
