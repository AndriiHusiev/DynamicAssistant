package com.husiev.dynassist.components.main.summarysingle

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val SUMMARY_SINGLE_ARG = "single_summary"
const val summarySingleRoute = "summary_single"
const val summarySingleFullRoute = "$summarySingleRoute/{$SUMMARY_SINGLE_ARG}"

fun NavController.navigateToSummarySingle(singleArg: Int, navOptions: NavOptions? = null) {
	this.navigate("$summarySingleRoute/$singleArg", navOptions)
}

fun NavGraphBuilder.summarySingleScreen() {
	composable(
		route = summarySingleFullRoute,
		arguments = listOf(
			navArgument(SUMMARY_SINGLE_ARG) { type = NavType.IntType },
		),
	) { navBackStackEntry ->
		SingleSummaryContent()
	}
}