package com.rafaeltmbr.stopwatch.core.use_cases.impl

import com.rafaeltmbr.stopwatch.core.data.repositories.StopwatchRepository
import com.rafaeltmbr.stopwatch.core.data.stores.StateStore
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.use_cases.SaveStopwatchStateUseCase

class SaveStopwatchStateUseCaseImpl(
    private val store: StateStore<StopwatchState>,
    private val repository: StopwatchRepository,
) : SaveStopwatchStateUseCase {
    override suspend fun execute() {
        repository.save(store.state.value)
    }
}