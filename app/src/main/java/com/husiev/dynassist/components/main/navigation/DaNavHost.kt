package com.husiev.dynassist.components.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.husiev.dynassist.components.main.composables.SortTechnics
import com.husiev.dynassist.components.main.utils.AccountClanInfo
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.VehicleShortData

@Composable
fun DaNavHost(
	navController: NavHostController,
	personalData: AccountPersonalData?,
	accountStatisticsData: Map<String, List<AccountStatisticsData>>,
	clanData: AccountClanInfo?,
	shortData: List<VehicleShortData>,
	sort: SortTechnics,
	onSummaryClick: (String) -> Unit,
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
			summaryData = accountStatisticsData,
			onClick = onSummaryClick
		)
		
		technicsScreen(
			shortData = shortData,
			sort = sort,
			onClick = onTechnicsClick
		)
		
		sessionsScreen(onClick = {})
		
		detailsScreen(
			detailsData = personalData,
			clanData = clanData,
		)
		
		summarySingleScreen(
			summaryData = accountStatisticsData,
		)
		
		technicsSingleScreen(
			shortData = shortData,
		)
	}
}