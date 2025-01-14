package com.rafaeltmbr.stopwatch.infra.presentation.navigation.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rafaeltmbr.stopwatch.domain.stores.MutableStateStore
import com.rafaeltmbr.stopwatch.infra.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.entities.PresentationState
import com.rafaeltmbr.stopwatch.infra.presentation.entities.Screen
import com.rafaeltmbr.stopwatch.infra.presentation.navigation.StackNavigator
import com.rafaeltmbr.stopwatch.infra.presentation.theme.StopwatchTheme
import com.rafaeltmbr.stopwatch.infra.presentation.views.HomeView
import com.rafaeltmbr.stopwatch.infra.presentation.views.LapsView

class StackNavigatorImpl(
    private val presentationStore: MutableStateStore<PresentationState>
) : StackNavigator {
    private var navController: NavHostController? = null

    override suspend fun push(screen: Screen) {
        presentationStore.update {
            if (navController == null || it.screens.contains(screen)) return@update it

            navController?.navigate(screen.name)
            it.copy(screens = it.screens + screen)
        }
    }

    override suspend fun pop() {
        presentationStore.update {
            if (navController == null || it.screens.size < 2) return@update it

            navController?.popBackStack()
            it.copy(screens = it.screens.subList(0, it.screens.size - 1))
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
                startDestination = Screen.Home.name,
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
                composable(Screen.Home.name) {
                    HomeView(viewModelFactory = homeViewModelFactory)
                }

                composable(Screen.Laps.name) {
                    LapsView(viewModelFactory = lapsViewModelFactory)
                }
            }
        }
    }
}