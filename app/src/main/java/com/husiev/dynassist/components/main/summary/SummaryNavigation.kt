package com.husiev.dynassist.components.main.summary

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.husiev.dynassist.components.main.details.detailsNavigationRoute

const val summaryNavigationRoute = "summary_route"

fun NavController.navigateToSummary(navOptions: NavOptions? = null) {
	this.navigate(summaryNavigationRoute, navOptions)
}

fun NavGraphBuilder.summaryScreen(
	onClick: (Int) -> Unit
) {
	composable(
		route = summaryNavigationRoute,
		enterTransition = { slideInHorizontally(tween()) { it } + fadeIn(tween()) },
		exitTransition = {
			if (targetState.destination.route == detailsNavigationRoute)
				slideOutVertically(tween()) { it } + fadeOut(tween())
			else
				slideOutHorizontally(tween()) { -it } + fadeOut(tween())
		},
		popEnterTransition = {
			if (initialState.destination.route == detailsNavigationRoute)
				slideInVertically(tween()) { it } + fadeIn(tween())
			else
				slideInHorizontally(tween()) { -it } + fadeIn(tween())
		},
		popExitTransition = { slideOutHorizontally(tween()) { it } + fadeOut(tween()) }
	) {
		SummaryContent(onClick = onClick)
	}
}