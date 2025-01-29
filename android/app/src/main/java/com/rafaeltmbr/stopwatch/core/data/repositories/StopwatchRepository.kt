package com.rafaeltmbr.stopwatch.core.data.repositories

import com.rafaeltmbr.stopwatch.core.entities.StopwatchState

interface StopwatchRepository {
    suspend fun load(): StopwatchState?
    suspend fun save(state: StopwatchState)
}