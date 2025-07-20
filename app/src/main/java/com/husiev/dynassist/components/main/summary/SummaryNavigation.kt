package com.husiev.dynassist.components.main.summary

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val summaryNavigationRoute = "summary_route"

fun NavController.navigateToSummary(navOptions: NavOptions? = null) {
	this.navigate(summaryNavigationRoute, navOptions)
}

fun NavGraphBuilder.summaryScreen(
	onClick: (Int) -> Unit
) {
	composable(
		route = summaryNavigationRoute,
	) {
		SummaryContent(onClick = onClick)
	}
}