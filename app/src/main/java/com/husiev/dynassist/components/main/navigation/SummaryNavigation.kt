package com.husiev.dynassist.components.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.husiev.dynassist.components.main.composables.SummaryContent
import com.husiev.dynassist.components.main.utils.AccountStatisticsData

const val summaryNavigationRoute = "summary_route"

fun NavController.navigateToSummary(navOptions: NavOptions? = null) {
	this.navigate(summaryNavigationRoute, navOptions)
}

fun NavGraphBuilder.summaryScreen(
	summaryData: List<AccountStatisticsData>,
	onClick: (String) -> Unit
) {
	composable(
		route = summaryNavigationRoute,
	) {
		SummaryContent(
			summaryData = summaryData,
			onClick = onClick
		)
	}
}