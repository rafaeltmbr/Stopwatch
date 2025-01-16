package com.rafaeltmbr.stopwatch.domain.repositories

import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState

interface StopwatchRepository {
    suspend fun load(): StopwatchState?
    suspend fun save(state: StopwatchState)
}