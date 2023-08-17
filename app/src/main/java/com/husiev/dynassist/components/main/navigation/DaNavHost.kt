package com.husiev.dynassist.components.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.husiev.dynassist.components.main.utils.DaAppState

@Composable
fun DaNavHost(
	appState: DaAppState,
	modifier: Modifier = Modifier,
	startDestination: String = summaryNavigationRoute,
) {
	val navController = appState.navController
	
	NavHost(
		navController = navController,
		startDestination = startDestination,
		modifier = modifier,
	) {
		summaryScreen(onClick = {})
		
		technicsScreen(onClick = {})
		
		sessionsScreen(onClick = {})
	}
}