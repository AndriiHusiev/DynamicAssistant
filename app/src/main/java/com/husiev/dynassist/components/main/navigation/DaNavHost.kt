package com.husiev.dynassist.components.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.MainRoutesData

@Composable
fun DaNavHost(
	navController: NavHostController,
	mainRoutesData: MainRoutesData,
	personalData: AccountPersonalData?,
	accountStatisticsData: AccountStatisticsData?,
	modifier: Modifier = Modifier,
	startDestination: String = summaryNavigationRoute,
) {
	NavHost(
		navController = navController,
		startDestination = startDestination,
		modifier = modifier,
	) {
		summaryScreen(
			summaryHeaders = mainRoutesData,
			summaryData = accountStatisticsData,
			onClick = {}
		)
		
		technicsScreen(onClick = {})
		
		sessionsScreen(onClick = {})
	}
}