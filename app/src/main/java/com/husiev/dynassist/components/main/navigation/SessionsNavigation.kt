package com.husiev.dynassist.components.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.husiev.dynassist.components.main.composables.SessionsContent

const val sessionsNavigationRoute = "sessions_route"

fun NavController.navigateToSessions(navOptions: NavOptions? = null) {
	this.navigate(sessionsNavigationRoute, navOptions)
}

fun NavGraphBuilder.sessionsScreen(onClick: (String) -> Unit) {
	composable(
		route = sessionsNavigationRoute,
	) {
		SessionsContent(onClick = onClick)
	}
}