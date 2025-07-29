package com.husiev.dynassist.components.main.details

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val detailsNavigationRoute = "details_route"

fun NavController.navigateToDetails(navOptions: NavOptions? = null) {
	this.navigate(detailsNavigationRoute, navOptions)
}

fun NavGraphBuilder.detailsScreen() {
	composable(
		route = detailsNavigationRoute,
		enterTransition = { slideInVertically(tween()) { -it } + fadeIn(tween()) },
		exitTransition = { slideOutVertically(tween()) { it } + fadeOut(tween()) },
		popEnterTransition = { slideInVertically(tween()) { it } + fadeIn(tween()) },
		popExitTransition = { slideOutVertically(tween()) { -it } + fadeOut(tween()) }
	) {
		DetailsContent()
	}
}