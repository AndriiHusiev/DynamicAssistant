package com.husiev.dynassist.components.main.utils

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.husiev.dynassist.components.main.details.navigateToDetails
import com.husiev.dynassist.components.main.sessions.navigateToSessions
import com.husiev.dynassist.components.main.summary.navigateToSummary
import com.husiev.dynassist.components.main.summarysingle.navigateToSummarySingle
import com.husiev.dynassist.components.main.technics.navigateToTechnics
import com.husiev.dynassist.components.main.technicssingle.navigateToTechnicsSingle

@Composable
fun rememberDaAppState(
	windowSizeClass: WindowSizeClass,
	navController: NavHostController = rememberNavController(),
): DaAppState = remember(
	windowSizeClass,
	navController,
) {
	DaAppState(
		windowSizeClass = windowSizeClass,
		navController = navController,
	)
}

class DaAppState(
	val windowSizeClass: WindowSizeClass,
	val navController: NavHostController,
) {
	val shouldShowBottomBar: Boolean
		get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
	
	val shouldShowNavRail: Boolean
		get() = !shouldShowBottomBar
	
	val currentDestination: NavDestination?
		@Composable get() = navController.currentBackStackEntryAsState().value?.destination
	
	/**
	 * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
	 * route.
	 */
	val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries
	
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
		val navOptions = navOptions { launchSingleTop = true }
		navController.navigateToDetails(navOptions)
	}
	
	fun navigateToSummarySingle(singleArg: Int) {
		val navOptions = navOptions { launchSingleTop = true }
		navController.navigateToSummarySingle(singleArg, navOptions)
	}
	
	fun navigateToTechnicsSingle(singleArg: Int) {
		val navOptions = navOptions { launchSingleTop = true }
		navController.navigateToTechnicsSingle(singleArg, navOptions)
	}
	
	@Composable
	fun getStringDestArg(key: String): String? =
		 navController.currentBackStackEntryAsState().value?.arguments?.getString(key)
	
	@Composable
	fun getIntDestArg(key: String): Int? =
		 navController.currentBackStackEntryAsState().value?.arguments?.getInt(key)
}