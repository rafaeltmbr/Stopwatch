package com.rafaeltmbr.stopwatch.domain.repositories.impl

import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.repositories.StopwatchRepository

interface StopwatchDataSource {
    suspend fun load(): StopwatchState?
    suspend fun save(state: StopwatchState)
}

class StopwatchRepositoryImpl(
    private val dao: StopwatchDataSource
) : StopwatchRepository {
    override suspend fun load() = dao.load()

    override suspend fun save(state: StopwatchState) = dao.save(state)
}