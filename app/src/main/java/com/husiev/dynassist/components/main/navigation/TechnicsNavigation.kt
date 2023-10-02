package com.husiev.dynassist.components.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.husiev.dynassist.components.main.composables.SortTechnics
import com.husiev.dynassist.components.main.composables.TechnicsContent
import com.husiev.dynassist.components.main.utils.VehicleShortData

const val technicsNavigationRoute = "technics_route"

fun NavController.navigateToTechnics(navOptions: NavOptions? = null) {
	this.navigate(technicsNavigationRoute, navOptions)
}

fun NavGraphBuilder.technicsScreen(
	shortData: List<VehicleShortData>,
	sort: SortTechnics,
	onClick: (Int) -> Unit
) {
	composable(
		route = technicsNavigationRoute,
	) {
		TechnicsContent(
			shortData = shortData,
			sort = sort,
			onClick = onClick
		)
	}
}