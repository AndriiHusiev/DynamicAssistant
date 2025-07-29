package com.husiev.dynassist.components.main.technicssingle

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.husiev.dynassist.components.main.sessions.sessionsNavigationRoute

const val TECHNICS_SINGLE_ARG = "single_id"
const val technicsSingleRoute = "technics_single"
const val technicsSingleFullRoute = "$technicsSingleRoute/{$TECHNICS_SINGLE_ARG}"

fun NavController.navigateToTechnicsSingle(singleArg: Int, navOptions: NavOptions? = null) {
	this.navigate("$technicsSingleRoute/$singleArg", navOptions)
}

fun NavGraphBuilder.technicsSingleScreen(
	upPress: () -> Unit
) {
	composable(
		route = technicsSingleFullRoute,
		arguments = listOf(
			navArgument(TECHNICS_SINGLE_ARG) { type = NavType.IntType },
		),
		enterTransition = { slideInHorizontally(tween()) { it } + fadeIn(tween()) },
		exitTransition = { slideOutHorizontally(tween()) { -it } + fadeOut(tween()) },
		popEnterTransition = { slideInHorizontally(tween()) { -it } + fadeIn(tween()) },
		popExitTransition = { slideOutHorizontally(tween()) { it } + fadeOut(tween()) }
	) { navBackStackEntry ->
		SingleTechnicsContent(
			upPress = upPress
		)
	}
}