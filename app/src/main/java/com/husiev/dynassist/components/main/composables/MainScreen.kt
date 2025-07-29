package com.husiev.dynassist.components.main.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.MainViewModel
import com.husiev.dynassist.components.main.details.detailsNavigationRoute
import com.husiev.dynassist.components.main.navigation.DaNavHost
import com.husiev.dynassist.components.main.sessions.sessionsNavigationRoute
import com.husiev.dynassist.components.main.summary.summaryNavigationRoute
import com.husiev.dynassist.components.main.summarysingle.SUMMARY_SINGLE_ARG
import com.husiev.dynassist.components.main.summarysingle.summarySingleFullRoute
import com.husiev.dynassist.components.main.technics.technicsNavigationRoute
import com.husiev.dynassist.components.main.utils.DaAppState
import com.husiev.dynassist.components.main.utils.rememberDaAppState
import com.husiev.dynassist.components.start.composables.NotifyEnum
import com.husiev.dynassist.network.dataclasses.DataState

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
	val title = remember(appState.currentDestination) {
		determineTitle(
			nickname = mainViewModel.nickname,
			paramTitles = mainViewModel.getParamTitles(),
			navController = appState.navController
		)
	}
	
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
		topBar = {
			MainTopBar(
				title = title,
				currentDestination = appState.currentDestination,
				onReloadClick = mainViewModel::getAccountAllData,
				onSortClick = { showSortDialog = true },
				onFilterClick = { showFilterDialog = true },
				navigateUp = appState::navigateUp,
				navigateToDetails = appState::navigateToDetails,
			)
		},
		bottomBar = {
			MainBottomBar(
				show = appState.shouldShowBottomBar,
				destinations = appState.topLevelDestinations,
				currentDestination = appState.currentDestination,
				onNavigateToDestination = appState::navigateToTopLevelDestination,
			)
		},
	) { innerPadding ->
		Row(
			Modifier.padding(innerPadding),
		) {
			Box(modifier = Modifier.animateContentSize()) {
				if (appState.shouldShowNavRail) {
					MainNavigationRail(
						destinations = appState.topLevelDestinations,
						onNavigateToDestination = appState::navigateToTopLevelDestination,
						currentDestination = appState.currentDestination,
					)
				}
			}
			
			Box(Modifier.fillMaxSize()) {
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

fun determineTitle(
	nickname: String,
	paramTitles: List<Pair<Int, String>>,
	navController: NavHostController
): String {
	val currentDestination = navController.currentDestination
	
	return when(currentDestination?.route) {
		
		summaryNavigationRoute,
		technicsNavigationRoute,
		sessionsNavigationRoute,
		detailsNavigationRoute -> nickname
		
		summarySingleFullRoute -> {
			val argKeys = currentDestination.arguments.keys
			if (argKeys.isNotEmpty()) {
				val paramId = navController.currentBackStackEntry?.arguments?.getInt(SUMMARY_SINGLE_ARG)
				paramTitles.find { it.first == paramId }?.second ?: nickname
			} else nickname
		}
		
		else -> nickname
	}
}