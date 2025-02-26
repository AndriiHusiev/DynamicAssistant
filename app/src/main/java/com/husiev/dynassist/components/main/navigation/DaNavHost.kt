package com.husiev.dynassist.components.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.husiev.dynassist.components.main.composables.FilterTechnics
import com.husiev.dynassist.components.main.composables.SortTechnics
import com.husiev.dynassist.components.main.utils.AccountClanInfo
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.VehicleData
import com.husiev.dynassist.components.start.composables.NotifyEnum

@Composable
fun DaNavHost(
	navController: NavHostController,
	notifyState: NotifyEnum,
	personalData: AccountPersonalData?,
	accountStatisticsData: Map<String, List<AccountStatisticsData>>,
	clanData: AccountClanInfo?,
	vehicleData: List<VehicleData>,
	sort: SortTechnics,
	filter: FilterTechnics,
	onSummaryClick: (String) -> Unit,
	onTechnicsClick: (Int) -> Unit,
	onNotifyClick: (Boolean) -> Unit,
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
			vehicleData = vehicleData,
			sort = sort,
			filter = filter,
			onClick = onTechnicsClick
		)
		
		sessionsScreen(onClick = {})
		
		detailsScreen(
			detailsData = personalData,
			clanData = clanData,
			notifyState = notifyState,
			onNotifyClick = onNotifyClick,
		)
		
		summarySingleScreen(
			summaryData = accountStatisticsData,
		)
		
		technicsSingleScreen(
			vehicleData = vehicleData,
		)
	}
}