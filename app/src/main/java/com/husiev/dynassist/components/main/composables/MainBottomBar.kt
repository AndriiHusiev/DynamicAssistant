package com.husiev.dynassist.components.main.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
			)
		}
	}
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
	this?.hierarchy?.any {
		it.route?.contains(destination.name, true) ?: false
	} ?: false