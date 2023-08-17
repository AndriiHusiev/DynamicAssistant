package com.husiev.dynassist.components.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.husiev.dynassist.components.main.composables.TechnicsContent

const val technicsNavigationRoute = "technics_route"

fun NavController.navigateToTechnics(navOptions: NavOptions? = null) {
	this.navigate(technicsNavigationRoute, navOptions)
}

fun NavGraphBuilder.technicsScreen(onClick: (String) -> Unit) {
	composable(
		route = technicsNavigationRoute,
	) {
		TechnicsContent(onClick = onClick)
	}
}