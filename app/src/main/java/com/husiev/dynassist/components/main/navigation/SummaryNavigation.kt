package com.husiev.dynassist.components.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.husiev.dynassist.components.main.composables.SummaryContent
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.MainRoutesData

const val summaryNavigationRoute = "summary_route"

fun NavController.navigateToSummary(navOptions: NavOptions? = null) {
	this.navigate(summaryNavigationRoute, navOptions)
}

fun NavGraphBuilder.summaryScreen(
	summaryHeaders: MainRoutesData,
	summaryData: AccountStatisticsData?,
	onClick: (String) -> Unit
) {
	composable(
		route = summaryNavigationRoute,
	) {
		SummaryContent(
			summaryHeaders = summaryHeaders,
			summaryData = summaryData,
			onClick = onClick
		)
	}
}