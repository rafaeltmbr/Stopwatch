package com.rafaeltmbr.stopwatch.domain.data.repositories.impl

import com.rafaeltmbr.stopwatch.domain.data.data_sources.StopwatchDataSource
import com.rafaeltmbr.stopwatch.domain.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState


class StopwatchRepositoryImpl(
    private val dataSource: StopwatchDataSource
) : StopwatchRepository {
    override suspend fun load() = dataSource.load()

    override suspend fun save(state: StopwatchState) = dataSource.save(state)
}