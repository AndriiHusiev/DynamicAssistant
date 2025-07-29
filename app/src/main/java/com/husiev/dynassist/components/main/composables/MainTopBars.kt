package com.husiev.dynassist.components.main.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.sessions.sessionsNavigationRoute
import com.husiev.dynassist.components.main.summary.summaryNavigationRoute
import com.husiev.dynassist.components.main.technics.technicsNavigationRoute
import com.husiev.dynassist.components.main.technicssingle.technicsSingleFullRoute
import com.husiev.dynassist.components.start.composables.DaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
	title: String,
	currentDestination: NavDestination?,
	onReloadClick: () -> Unit,
	onSortClick: () -> Unit,
	onFilterClick: () -> Unit,
	navigateUp: () -> Unit,
	navigateToDetails: () -> Unit,
	modifier: Modifier = Modifier,
) {
	var navigationIconContentDescription: String? = null
	var actionIconContentDescription: String? = null
	var navigationIcon: ImageVector? = null
	var actionIcon: ImageVector? = null
	var onNavigationClick: () -> Unit = {}
	var onActionClick: () -> Unit = {}
	var show = true
	
	currentDestination?.let {
		when(it.route) {
			summaryNavigationRoute,
			sessionsNavigationRoute -> {
				navigationIcon = Icons.Outlined.Info
				actionIcon = Icons.Filled.Refresh
				navigationIconContentDescription = stringResource(R.string.description_account_details)
				actionIconContentDescription = stringResource(R.string.description_refresh)
				onNavigationClick = {
					navigateToDetails()
				}
				onActionClick = {
					onReloadClick()
				}
			}
			
			technicsNavigationRoute -> {
				navigationIcon = Icons.AutoMirrored.Filled.Sort
				actionIcon = Icons.Filled.FilterList
				navigationIconContentDescription = stringResource(R.string.description_sort_list)
				actionIconContentDescription = stringResource(R.string.description_filter_list)
				onNavigationClick = {
					onSortClick()
				}
				onActionClick = {
					onFilterClick()
				}
			}
			
			technicsSingleFullRoute -> {
				show = false
			}
			
			else -> {
				navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
				navigationIconContentDescription = stringResource(R.string.description_back)
				onNavigationClick = { navigateUp() }
			}
		}
	}
	
	Box(modifier = Modifier.animateContentSize()) {
		if (show) {
			DaTopAppBar(
				title = title,
				modifier = modifier,
				navigationIcon = navigationIcon,
				navigationIconContentDescription = navigationIconContentDescription,
				onNavigationClick = onNavigationClick,
				actionIcon = actionIcon,
				actionIconContentDescription = actionIconContentDescription,
				onActionClick = onActionClick,
			)
		}
	}
}