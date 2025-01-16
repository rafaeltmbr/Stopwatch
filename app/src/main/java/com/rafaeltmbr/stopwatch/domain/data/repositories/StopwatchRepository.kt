package com.rafaeltmbr.stopwatch.domain.data.repositories

import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState

interface StopwatchRepository {
    suspend fun load(): StopwatchState?
    suspend fun save(state: StopwatchState)
}