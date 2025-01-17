package com.rafaeltmbr.stopwatch.infra.presentation.navigation

import com.rafaeltmbr.stopwatch.domain.data.stores.impl.MutableStateStoreImpl
import com.rafaeltmbr.stopwatch.infra.presentation.compose.navigation.StackNavigatorImpl
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.entities.Screen
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StackNavigatorTest {
    @Test
    fun push_shouldPushNewScreen() = runTest {
        val presentationStore = MutableStateStoreImpl(PresentationState(screens = emptyList()))
        val stackNavigator = StackNavigatorImpl(presentationStore)

        stackNavigator.push(Screen.Home)
        Assert.assertEquals(
            PresentationState(screens = listOf(Screen.Home)),
            presentationStore.state.value
        )

        stackNavigator.push(Screen.Laps)
        Assert.assertEquals(
            PresentationState(screens = listOf(Screen.Home, Screen.Laps)),
            presentationStore.state.value
        )
    }

    @Test
    fun push_shouldNotPushScreenIfAlreadyOnStack() = runTest {
        val presentationStore =
            MutableStateStoreImpl(PresentationState(screens = listOf(Screen.Home, Screen.Laps)))
        val stackNavigator = StackNavigatorImpl(presentationStore)

        stackNavigator.push(Screen.Home)
        stackNavigator.push(Screen.Laps)
        Assert.assertEquals(
            PresentationState(screens = listOf(Screen.Home, Screen.Laps)),
            presentationStore.state.value
        )
    }

    @Test
    fun pop_shouldPopScreen() = runTest {
        val presentationStore =
            MutableStateStoreImpl(PresentationState(screens = listOf(Screen.Home, Screen.Laps)))
        val stackNavigator = StackNavigatorImpl(presentationStore)

        stackNavigator.pop()
        Assert.assertEquals(
            PresentationState(screens = listOf(Screen.Home)),
            presentationStore.state.value
        )
    }

    @Test
    fun pop_shouldNotPopScreenIfTheresOnlyOnScreen() = runTest {
        val presentationStore =
            MutableStateStoreImpl(PresentationState(screens = listOf(Screen.Home)))
        val stackNavigator = StackNavigatorImpl(presentationStore)

        stackNavigator.pop()
        Assert.assertEquals(
            PresentationState(screens = listOf(Screen.Home)),
            presentationStore.state.value
        )
    }
}