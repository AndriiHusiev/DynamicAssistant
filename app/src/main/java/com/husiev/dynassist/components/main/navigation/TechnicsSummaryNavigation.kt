package com.husiev.dynassist.components.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.husiev.dynassist.components.main.composables.SingleTechnicsContent
import com.husiev.dynassist.components.main.utils.VehicleShortData

const val TECHNICS_SINGLE_ARG = "single_id"
const val technicsSingleRoute = "technics_single"

fun NavController.navigateToTechnicsSingle(singleArg: Int, navOptions: NavOptions? = null) {
	this.navigate("$technicsSingleRoute/$singleArg", navOptions)
}

fun NavGraphBuilder.technicsSingleScreen(
	shortData: List<VehicleShortData>,
) {
	composable(
		route = "$technicsSingleRoute/{$TECHNICS_SINGLE_ARG}",
		arguments = listOf(
			navArgument(TECHNICS_SINGLE_ARG) { type = NavType.IntType },
		),
	) { navBackStackEntry ->
		val singleArg = navBackStackEntry.arguments?.getInt(TECHNICS_SINGLE_ARG)
		SingleTechnicsContent(
			shortData = shortData,
			singleId = singleArg,
		)
	}
}