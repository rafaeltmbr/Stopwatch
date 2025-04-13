package com.rafaeltmbr.stopwatch.core.use_cases.impl

import com.rafaeltmbr.stopwatch.core.data.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.core.use_cases.NewLapUseCase

class NewLapUseCaseImpl(
    private val store: MutableStateStore<StopwatchState>,
) : NewLapUseCase {
    override suspend fun execute() {
        store.update {
            it.copy().lap()
        }
    }
}