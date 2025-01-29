package com.rafaeltmbr.stopwatch.core.data.repositories.impl

import com.rafaeltmbr.stopwatch.core.data.data_sources.StopwatchDataSourcePort
import com.rafaeltmbr.stopwatch.core.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState


class StopwatchRepositoryImpl(
    private val dataSource: StopwatchDataSourcePort
) : StopwatchRepository {
    override suspend fun load() = dataSource.load()

    override suspend fun save(state: StopwatchState) = dataSource.save(state)
}