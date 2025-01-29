package com.rafaeltmbr.stopwatch.platform.presentation.navigation

import com.rafaeltmbr.stopwatch.platform.presentation.compose.navigation.StackNavigatorImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StackNavigatorTest {
    @Test
    fun push_shouldPushNewScreen() = runTest {
        val stackNavigator = StackNavigatorImpl(emptyList())

        stackNavigator.push(StackNavigator.Screen.Home)
        Assert.assertEquals(
            listOf(StackNavigator.Screen.Home),
            stackNavigator.stack
        )

        stackNavigator.push(StackNavigator.Screen.Laps)
        Assert.assertEquals(
            listOf(StackNavigator.Screen.Home, StackNavigator.Screen.Laps),
            stackNavigator.stack
        )
    }

    @Test
    fun push_shouldNotPushScreenIfAlreadyOnStack() = runTest {
        val stackNavigator = StackNavigatorImpl(emptyList())

        stackNavigator.push(StackNavigator.Screen.Home)
        stackNavigator.push(StackNavigator.Screen.Laps)
        Assert.assertEquals(
            listOf(StackNavigator.Screen.Home, StackNavigator.Screen.Laps),
            stackNavigator.stack
        )
    }

    @Test
    fun pop_shouldPopScreen() = runTest {
        val stackNavigator = StackNavigatorImpl(
            listOf(
                StackNavigator.Screen.Home,
                StackNavigator.Screen.Laps
            )
        )

        stackNavigator.pop()
        Assert.assertEquals(
            listOf(StackNavigator.Screen.Home),
            stackNavigator.stack
        )
    }

    @Test
    fun pop_shouldNotPopScreenIfTheresOnlyOnScreen() = runTest {
        val stackNavigator = StackNavigatorImpl(listOf(StackNavigator.Screen.Home))

        stackNavigator.pop()
        Assert.assertEquals(
            listOf(StackNavigator.Screen.Home),
            stackNavigator.stack
        )
    }
}