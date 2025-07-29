package com.husiev.dynassist.components.main.sessions

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val sessionsNavigationRoute = "sessions_route"

fun NavController.navigateToSessions(navOptions: NavOptions? = null) {
	this.navigate(sessionsNavigationRoute, navOptions)
}

fun NavGraphBuilder.sessionsScreen(onClick: (String) -> Unit) {
	composable(
		route = sessionsNavigationRoute,
		enterTransition = { slideInHorizontally(tween()) { it } + fadeIn(tween()) },
		exitTransition = { slideOutHorizontally(tween()) { it } + fadeOut(tween()) },
		popEnterTransition = { slideInHorizontally(tween()) { -it } + fadeIn(tween()) },
		popExitTransition = { slideOutHorizontally(tween()) { it } + fadeOut(tween()) }
	) {
		SessionsContent(onClick = onClick)
	}
}