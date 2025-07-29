package com.husiev.dynassist.components.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.husiev.dynassist.components.main.composables.FilterTechnics
import com.husiev.dynassist.components.main.composables.SortTechnics
import com.husiev.dynassist.components.main.details.detailsScreen
import com.husiev.dynassist.components.main.sessions.sessionsScreen
import com.husiev.dynassist.components.main.summary.summaryNavigationRoute
import com.husiev.dynassist.components.main.summary.summaryScreen
import com.husiev.dynassist.components.main.summarysingle.summarySingleScreen
import com.husiev.dynassist.components.main.technics.technicsScreen
import com.husiev.dynassist.components.main.technicssingle.technicsSingleScreen

@Composable
fun DaNavHost(
	navController: NavHostController,
	sort: SortTechnics,
	filter: FilterTechnics,
	onSummaryClick: (Int) -> Unit,
	onTechnicsClick: (Int) -> Unit,
	modifier: Modifier = Modifier,
	startDestination: String = summaryNavigationRoute,
) {
	NavHost(
		navController = navController,
		startDestination = startDestination,
		modifier = modifier,
	) {
		summaryScreen(
			onClick = onSummaryClick
		)
		
		technicsScreen(
			sort = sort,
			filter = filter,
			onClick = onTechnicsClick
		)
		
		sessionsScreen(onClick = {})
		
		detailsScreen()
		
		summarySingleScreen()
		
		technicsSingleScreen(
			upPress = navController::navigateUp
		)
	}
}