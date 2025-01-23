package com.rafaeltmbr.stopwatch.domain.use_cases.impl

import com.rafaeltmbr.stopwatch.domain.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.domain.data.stores.StateStore
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.use_cases.SaveStopwatchStateUseCase

class SaveStopwatchStateUseCaseImpl(
    private val store: StateStore<StopwatchState>,
    private val repository: StopwatchRepository,
) : SaveStopwatchStateUseCase {
    override suspend fun execute() {
        repository.save(store.state.value)
    }
}