package com.husiev.dynassist.components.main.details

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
	) {
		DetailsContent()
	}
}