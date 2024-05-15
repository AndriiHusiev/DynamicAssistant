package com.husiev.dynassist.components.main.composables

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
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.MainViewModel
import com.husiev.dynassist.components.main.navigation.sessionsNavigationRoute
import com.husiev.dynassist.components.main.navigation.summaryNavigationRoute
import com.husiev.dynassist.components.main.navigation.technicsNavigationRoute
import com.husiev.dynassist.components.main.utils.DaAppState
import com.husiev.dynassist.components.start.composables.DaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
	mainViewModel: MainViewModel,
	appState: DaAppState,
	onSortClick: () -> Unit,
	onFilterClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	var title = mainViewModel.nickname
	var navigationIconContentDescription: String? = null
	var actionIconContentDescription: String? = null
	var navigationIcon: ImageVector? = null
	var actionIcon: ImageVector? = null
	var onNavigationClick: () -> Unit = {}
	var onActionClick: () -> Unit = {}
	
	appState.currentDestination?.let {
		when(it.route) {
			summaryNavigationRoute,
			sessionsNavigationRoute -> {
				navigationIcon = Icons.Outlined.Info
				actionIcon = Icons.Filled.Refresh
				navigationIconContentDescription = stringResource(R.string.description_account_details)
				actionIconContentDescription = stringResource(R.string.description_refresh)
				onNavigationClick = {
					appState.navigateToDetails()
				}
				onActionClick = {
					mainViewModel.getAccountAllData()
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
			
			else -> {
				val argKeys = it.arguments.keys
				if (argKeys.isNotEmpty()) {
					when(val arg = argKeys.first()) {
						"single_title" -> appState.getStringDestArg(arg)?.let { param ->
							title = param
						}
						"single_id" -> appState.getIntDestArg(arg)?.let { id ->
							title = mainViewModel.shortData.value.singleOrNull { vehicle ->
								vehicle.tankId == id
							}?.name ?: ""
						}
					}
				}
				navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
				navigationIconContentDescription = stringResource(R.string.description_back)
				onNavigationClick = { appState.navController.popBackStack() }
			}
		}
	}
	
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