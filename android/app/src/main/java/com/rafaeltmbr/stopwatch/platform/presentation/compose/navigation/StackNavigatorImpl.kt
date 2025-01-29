package com.rafaeltmbr.stopwatch.platform.presentation.compose.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rafaeltmbr.stopwatch.platform.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.platform.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.platform.presentation.compose.theme.StopwatchTheme
import com.rafaeltmbr.stopwatch.platform.presentation.compose.views.HomeView
import com.rafaeltmbr.stopwatch.platform.presentation.compose.views.LapsView
import com.rafaeltmbr.stopwatch.platform.presentation.navigation.StackNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StackNavigatorImpl(initialStack: List<StackNavigator.Screen>) : StackNavigator {
    private var _stack = MutableStateFlow(initialStack)
    private var navController: NavHostController? = null

    override val stack: StateFlow<List<StackNavigator.Screen>> = _stack.asStateFlow()

    override suspend fun push(screen: StackNavigator.Screen) {
        _stack.update {
            if (navController == null || it.contains(screen)) return@update it

            navController?.navigate(screen.name)
            it + screen
        }
    }

    override suspend fun pop() {
        _stack.update {
            if (navController == null || it.size < 2) return@update it

            navController?.popBackStack()
            it.subList(0, it.size - 1)
        }
    }

    @Composable
    fun NavigationStack(
        homeViewModelFactory: HomeViewModelFactory,
        lapsViewModelFactory: LapsViewModelFactory
    ) {
        val navController = rememberNavController()
        val self = this

        DisposableEffect(Unit) {
            self.navController = navController

            onDispose {
                self.navController = null
            }
        }

        StopwatchTheme {
            NavHost(
                navController = navController,
                startDestination = StackNavigator.Screen.Home.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                }
            ) {
                composable(StackNavigator.Screen.Home.name) {
                    HomeView(viewModelFactory = homeViewModelFactory)
                }

                composable(StackNavigator.Screen.Laps.name) {
                    LapsView(viewModelFactory = lapsViewModelFactory)
                }
            }
        }
    }
}