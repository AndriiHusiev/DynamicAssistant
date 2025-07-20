package com.husiev.dynassist.components.main.technicssingle

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val TECHNICS_SINGLE_ARG = "single_id"
const val technicsSingleRoute = "technics_single"

fun NavController.navigateToTechnicsSingle(singleArg: Int, navOptions: NavOptions? = null) {
	this.navigate("$technicsSingleRoute/$singleArg", navOptions)
}

fun NavGraphBuilder.technicsSingleScreen() {
	composable(
		route = "$technicsSingleRoute/{$TECHNICS_SINGLE_ARG}",
		arguments = listOf(
			navArgument(TECHNICS_SINGLE_ARG) { type = NavType.IntType },
		),
	) { navBackStackEntry ->
		val singleArg = navBackStackEntry.arguments?.getInt(TECHNICS_SINGLE_ARG)
		SingleTechnicsContent(singleId = singleArg)
	}
}