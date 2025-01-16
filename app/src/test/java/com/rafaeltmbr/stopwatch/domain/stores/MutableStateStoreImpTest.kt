package com.rafaeltmbr.stopwatch.domain.stores

import com.rafaeltmbr.stopwatch.domain.data.stores.impl.MutableStateStoreImpl
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MutableStateStoreImpTest {
    @Test
    fun state_shouldBeInitializedWithInitialValue() = runTest {
        val initial = Uuid.random()
        val store = MutableStateStoreImpl(initial)
        Assert.assertEquals(initial, store.state.value)
    }

    @Test
    fun update_shouldPassTheCurrentStateToTheUpdateCallback() = runTest {
        val initial = Uuid.random()
        val store = MutableStateStoreImpl(initial)
        store.update {
            Assert.assertEquals(initial, it)
            it
        }
    }

    @Test
    fun update_shouldUpdateStateImmediately() = runTest {
        val store = MutableStateStoreImpl(17)
        store.update { it + 23 }
        Assert.assertEquals(40, store.state.value)
    }

    @Test
    fun update_shouldEmitStateChanges() = runTest {
        val store = MutableStateStoreImpl("Initial State")
        var emitted = ""

        val listenToUpdates = async {
            emitted = store.state.first()
        }

        store.update { it.uppercase() }

        listenToUpdates.await()

        Assert.assertEquals(store.state.value, emitted)
    }
}