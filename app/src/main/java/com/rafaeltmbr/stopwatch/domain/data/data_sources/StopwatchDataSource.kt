package com.rafaeltmbr.stopwatch.domain.data.data_sources

import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState

interface StopwatchDataSource {
    suspend fun load(): StopwatchState?
    suspend fun save(state: StopwatchState)
}
