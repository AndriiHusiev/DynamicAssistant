package com.husiev.dynassist.components.main.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.husiev.dynassist.components.main.navigation.TopLevelDestination

@Composable
fun MainBottomBar(
	destinations: List<TopLevelDestination>,
	currentDestination: NavDestination?,
	modifier: Modifier = Modifier,
	onNavigateToDestination: (TopLevelDestination) -> Unit = {},
) {
	
	NavigationBar(
		modifier = modifier,
	) {
		destinations.forEach { dst ->
			val selected = currentDestination.isTopLevelDestinationInHierarchy(dst)
			
			NavigationBarItem(
				selected = selected,
				onClick = { onNavigateToDestination(dst) },
				icon = {
					Icon(
						imageVector = if (selected) dst.selectedIcon else dst.unselectedIcon,
						contentDescription = null,
					)
				},
				label = { Text(stringResource(dst.iconTextId)) },
				colors = NavigationBarItemDefaults.colors(
					selectedIconColor = NavigationDefaults.navigationSelectedItemColor(),
					unselectedIconColor = NavigationDefaults.navigationContentColor(),
					selectedTextColor = NavigationDefaults.navigationSelectedItemColor(),
					unselectedTextColor = NavigationDefaults.navigationContentColor(),
					indicatorColor = NavigationDefaults.navigationIndicatorColor(),
				),
			)
		}
	}
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
	this?.hierarchy?.any {
		it.route?.contains(destination.name, true) ?: false
	} ?: false

@Composable
fun MainNavigationRail(
	destinations: List<TopLevelDestination>,
	onNavigateToDestination: (TopLevelDestination) -> Unit,
	currentDestination: NavDestination?,
	modifier: Modifier = Modifier,
) {
	NavigationRail(modifier = modifier) {
		destinations.forEach { dst ->
			val selected = currentDestination.isTopLevelDestinationInHierarchy(dst)
			
			NavigationRailItem(
				selected = selected,
				onClick = { onNavigateToDestination(dst) },
				icon = {
					Icon(
						imageVector = if (selected) dst.selectedIcon else dst.unselectedIcon,
						contentDescription = null,
					)
				},
				label = { Text(stringResource(dst.iconTextId)) },
				colors = NavigationRailItemDefaults.colors(
					selectedIconColor = NavigationDefaults.navigationSelectedItemColor(),
					unselectedIconColor = NavigationDefaults.navigationContentColor(),
					selectedTextColor = NavigationDefaults.navigationSelectedItemColor(),
					unselectedTextColor = NavigationDefaults.navigationContentColor(),
					indicatorColor = NavigationDefaults.navigationIndicatorColor(),
				),
			)
		}
	}
}

/**
 * Navigation default values.
 */
object NavigationDefaults {
	@Composable
	fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant
	
	@Composable
	fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer
	
	@Composable
	fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}