package com.husiev.dynassist.components.main.utils

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.husiev.dynassist.components.main.navigation.TopLevelDestination
import com.husiev.dynassist.components.main.navigation.TopLevelDestination.SESSIONS
import com.husiev.dynassist.components.main.navigation.TopLevelDestination.SUMMARY
import com.husiev.dynassist.components.main.navigation.TopLevelDestination.TECHNICS
import com.husiev.dynassist.components.main.navigation.navigateToDetails
import com.husiev.dynassist.components.main.navigation.navigateToSessions
import com.husiev.dynassist.components.main.navigation.navigateToSummary
import com.husiev.dynassist.components.main.navigation.navigateToTechnics
import com.husiev.dynassist.network.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Composable
fun rememberDaAppState(
	windowSizeClass: WindowSizeClass,
	coroutineScope: CoroutineScope = rememberCoroutineScope(),
	navController: NavHostController = rememberNavController(),
	networkRepository: NetworkRepository,
): DaAppState = remember(
	windowSizeClass,
	navController,
	networkRepository
) {
	DaAppState(
		windowSizeClass = windowSizeClass,
		coroutineScope = coroutineScope,
		navController = navController,
		networkRepository = networkRepository,
	)
}

class DaAppState(
	val windowSizeClass: WindowSizeClass,
	val coroutineScope: CoroutineScope,
	val navController: NavHostController,
	private val networkRepository: NetworkRepository,
) {
	val shouldShowBottomBar: Boolean
		get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
	
	val shouldShowNavRail: Boolean
		get() = !shouldShowBottomBar
	
	val currentDestination: NavDestination?
		@Composable get() = navController.currentBackStackEntryAsState().value?.destination
	
	var noConnection: StateFlow<Result<String>> = networkRepository.noConnection
	
	fun closeSnackbar() = networkRepository.noConnection.tryEmit(Result.StandBy)
	
	/**
	 * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
	 * route.
	 */
	val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()
	
	/**
	 * UI logic for navigating to a top level destination in the app. Top level destinations have
	 * only one copy of the destination of the back stack, and save and restore state whenever you
	 * navigate to and from it.
	 *
	 * @param topLevelDestination The destination the app needs to navigate to.
	 */
	fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
		val topLevelNavOptions = navOptions {
			// Pop up to the start destination of the graph to
			// avoid building up a large stack of destinations
			// on the back stack as users select items
			popUpTo(navController.graph.findStartDestination().id) {
				saveState = true
			}
			// Avoid multiple copies of the same destination when
			// reselecting the same item
			launchSingleTop = true
			// Restore state when reselecting a previously selected item
			restoreState = true
		}
		
		when (topLevelDestination) {
			SUMMARY -> navController.navigateToSummary(topLevelNavOptions)
			TECHNICS -> navController.navigateToTechnics(topLevelNavOptions)
			SESSIONS -> navController.navigateToSessions(topLevelNavOptions)
		}
	}
	
	fun navigateToDetails() {
		navController.navigateToDetails()
	}
}