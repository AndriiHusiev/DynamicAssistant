package com.husiev.dynassist.components.main.technics

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.husiev.dynassist.components.main.composables.FilterTechnics
import com.husiev.dynassist.components.main.composables.SortTechnics
import com.husiev.dynassist.components.main.sessions.sessionsNavigationRoute

const val technicsNavigationRoute = "technics_route"

fun NavController.navigateToTechnics(navOptions: NavOptions? = null) {
	this.navigate(technicsNavigationRoute, navOptions)
}

fun NavGraphBuilder.technicsScreen(
	sort: SortTechnics,
	filter: FilterTechnics,
	onClick: (Int) -> Unit
) {
	composable(
		route = technicsNavigationRoute,
		enterTransition = {
			if (initialState.destination.route == sessionsNavigationRoute)
				slideInHorizontally(tween()) { -it } + fadeIn(tween())
			else
				slideInHorizontally(tween()) { it } + fadeIn(tween())
		},
		exitTransition = { slideOutHorizontally(tween()) { -it } + fadeOut(tween()) },
		popEnterTransition = { slideInHorizontally(tween()) { -it } + fadeIn(tween()) },
		popExitTransition = { slideOutHorizontally(tween()) { it } + fadeOut(tween()) }
	) {
		TechnicsContent(
			sort = sort,
			filter = filter,
			onClick = onClick
		)
	}
}