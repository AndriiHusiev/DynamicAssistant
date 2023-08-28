package com.husiev.dynassist.components.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.husiev.dynassist.components.main.composables.DetailsContent
import com.husiev.dynassist.components.main.utils.AccountPersonalData

const val detailsNavigationRoute = "details_route"

fun NavController.navigateToDetails(navOptions: NavOptions? = null) {
	this.navigate(detailsNavigationRoute, navOptions)
}

fun NavGraphBuilder.detailsScreen(
	detailsData: AccountPersonalData?,
) {
	composable(
		route = detailsNavigationRoute,
	) {
		DetailsContent(
			detailsData = detailsData,
		)
	}
}