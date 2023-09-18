package com.husiev.dynassist.components.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.husiev.dynassist.components.main.composables.SingleSummaryContent
import com.husiev.dynassist.components.main.utils.AccountStatisticsData

const val SUMMARY_SINGLE_ARG = "single_title"
const val summarySingleRoute = "summary_single"

fun NavController.navigateToSummarySingle(singleArg: String, navOptions: NavOptions? = null) {
	this.navigate("$summarySingleRoute/$singleArg", navOptions)
}

fun NavGraphBuilder.summarySingleScreen(
	summaryData: Map<String, List<AccountStatisticsData>>,
) {
	composable(
		route = "$summarySingleRoute/{$SUMMARY_SINGLE_ARG}",
		arguments = listOf(
			navArgument(SUMMARY_SINGLE_ARG) { type = NavType.StringType },
		),
	) { navBackStackEntry ->
		val singleArg = navBackStackEntry.arguments?.getString(SUMMARY_SINGLE_ARG)
		SingleSummaryContent(
			summaryData = summaryData,
			singleTitle = singleArg,
		)
	}
}