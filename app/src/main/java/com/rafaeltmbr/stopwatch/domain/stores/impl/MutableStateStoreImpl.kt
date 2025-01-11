package com.rafaeltmbr.stopwatch.domain.stores.impl

import com.rafaeltmbr.stopwatch.domain.stores.MutableStateStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MutableStateStoreImpl<T>(value: T) : MutableStateStore<T> {
    private var _state = MutableStateFlow(value)
    private val mutex = Mutex()

    override val state: StateFlow<T> = _state.asStateFlow()

    override suspend fun update(cb: (T) -> T) = mutex.withLock {
        _state.update(cb)
    }
}