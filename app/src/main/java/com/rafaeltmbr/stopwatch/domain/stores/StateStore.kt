package com.rafaeltmbr.stopwatch.domain.stores

import kotlinx.coroutines.flow.StateFlow

interface StateStore<T> {
    val state: StateFlow<T>
}

interface MutableStateStore<T> : StateStore<T> {
    suspend fun update(cb: (T) -> T)
}