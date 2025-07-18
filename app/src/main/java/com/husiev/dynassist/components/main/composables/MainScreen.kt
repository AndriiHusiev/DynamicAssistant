package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.MainViewModel
import com.husiev.dynassist.components.main.navigation.DaNavHost
import com.husiev.dynassist.components.main.utils.DaAppState
import com.husiev.dynassist.network.dataclasses.DataState
import com.husiev.dynassist.components.main.utils.rememberDaAppState
import com.husiev.dynassist.components.start.composables.NotifyEnum

@Composable
fun MainScreen(
	windowSizeClass: WindowSizeClass,
	modifier: Modifier = Modifier,
	mainViewModel: MainViewModel = hiltViewModel(),
	appState: DaAppState = rememberDaAppState(
		windowSizeClass = windowSizeClass,
	),
) {
	val notifyState by mainViewModel.notifyState.collectAsStateWithLifecycle()
	val sortTechnics by mainViewModel.sortTechnics.collectAsStateWithLifecycle()
	val filterTechnics by mainViewModel.filterTechnics.collectAsStateWithLifecycle()
	val queryResult by mainViewModel.queryStatus.collectAsStateWithLifecycle()
	val snackbarHostState = remember { SnackbarHostState() }
	var showSortDialog by rememberSaveable { mutableStateOf(false) }
	if (showSortDialog) {
		TechnicsSortDialog(
			sort = sortTechnics,
			onDismiss = { showSortDialog = false },
			onChangeSort = mainViewModel::changeSortTechnics
		)
	}
	var showFilterDialog by rememberSaveable { mutableStateOf(false) }
	if (showFilterDialog) {
		TechnicsFilterDialog(
			filter = filterTechnics,
			onDismiss = { showFilterDialog = false },
			onChangeFilter = mainViewModel::changeFilterTechnics
		)
	}
	if (queryResult is DataState.Success<*> && notifyState == NotifyEnum.UPDATES_AVAIL) {
		mainViewModel.switchNotification(true)
	}
	
	val noConnectionMessage = stringResource(R.string.no_connection)
	val retryLabel = stringResource(R.string.retry)
	LaunchedEffect(queryResult) {
		if (queryResult is DataState.Error) {
			val result = snackbarHostState.showSnackbar(
				message = noConnectionMessage,
				actionLabel = retryLabel,
				withDismissAction = true,
				duration = SnackbarDuration.Indefinite
			)
			when(result) {
				SnackbarResult.ActionPerformed -> mainViewModel.getAccountAllData()
				SnackbarResult.Dismissed -> mainViewModel.closeSnackbar()
			}
		}
	}
	
	// Launch data loading once at startup
	LaunchedEffect(key1 = Unit) {
		mainViewModel.getAccountAllData()
	}
	
	Scaffold(
		modifier = modifier,
		containerColor = Color.Transparent,
		contentColor = MaterialTheme.colorScheme.onBackground,
		snackbarHost = { SnackbarHost(snackbarHostState) },
		bottomBar = {
			if (appState.shouldShowBottomBar) {
				MainBottomBar(
					destinations = appState.topLevelDestinations,
					currentDestination = appState.currentDestination,
					onNavigateToDestination = appState::navigateToTopLevelDestination,
				)
			}
		},
	) { innerPadding ->
		Row(
			Modifier.padding(innerPadding),
		) {
			if (appState.shouldShowNavRail) {
				MainNavigationRail(
					destinations = appState.topLevelDestinations,
					onNavigateToDestination = appState::navigateToTopLevelDestination,
					currentDestination = appState.currentDestination,
				)
			}
			
			Column(Modifier.fillMaxSize()) {
				MainTopBar(
					mainViewModel = mainViewModel,
					appState = appState,
					onSortClick = { showSortDialog = true },
					onFilterClick = { showFilterDialog = true },
				)
				
				Box {
					DaNavHost(
						navController = appState.navController,
						sort = sortTechnics,
						filter = filterTechnics,
						onSummaryClick = appState::navigateToSummarySingle,
						onTechnicsClick = appState::navigateToTechnicsSingle,
					)
					
					if (queryResult is DataState.Loading) {
						LinearProgressIndicator(modifier = modifier.fillMaxWidth())
					}
				}
			}
		}
	}
}