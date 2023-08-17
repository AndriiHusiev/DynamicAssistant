package com.husiev.dynassist.components.main.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import com.husiev.dynassist.R

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
	val selectedIcon: ImageVector,
	val unselectedIcon: ImageVector,
	@StringRes val iconTextId: Int,
) {
	SUMMARY(
		selectedIcon = Icons.Filled.Home,
		unselectedIcon = Icons.Outlined.Home,
		iconTextId = R.string.navigation_item_summary_text,
	),
	TECHNICS(
		selectedIcon = Icons.Filled.ViewList,
		unselectedIcon = Icons.Outlined.ViewList,
		iconTextId = R.string.navigation_item_technics_text,
	),
	SESSIONS(
		selectedIcon = Icons.Filled.Info,
		unselectedIcon = Icons.Outlined.Info,
		iconTextId = R.string.navigation_item_sessions_text,
	),
}