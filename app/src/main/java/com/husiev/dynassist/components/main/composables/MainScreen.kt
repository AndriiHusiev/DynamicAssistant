package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.components.main.MainViewModel
import com.husiev.dynassist.components.main.navigation.DaNavHost
import com.husiev.dynassist.components.main.utils.DaAppState
import com.husiev.dynassist.components.main.utils.rememberDaAppState
import com.husiev.dynassist.components.start.composables.DaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	modifier: Modifier = Modifier,
	mainViewModel: MainViewModel = hiltViewModel(),
	appState: DaAppState = rememberDaAppState(),
) {
	val personalData by mainViewModel.personalData.collectAsStateWithLifecycle()
	val statisticData by mainViewModel.statisticData.collectAsStateWithLifecycle()
	val queryResult by mainViewModel.queryResult.collectAsStateWithLifecycle()
	
	Scaffold(
		modifier = modifier,
		containerColor = Color.Transparent,
		contentColor = MaterialTheme.colorScheme.onBackground,
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
			Modifier
				.fillMaxSize()
				.padding(innerPadding),
		) {
			if (appState.shouldShowNavRail) {
				MainNavigationRail(
					destinations = appState.topLevelDestinations,
					onNavigateToDestination = appState::navigateToTopLevelDestination,
					currentDestination = appState.currentDestination,
				)
			}
			
			Column(Modifier.fillMaxSize()) {
				DaTopAppBar(
					title = mainViewModel.nickname,
				)
				
				DaNavHost(
					navController = appState.navController,
					mainRoutesData = mainViewModel.mainRoutesData,
					personalData = personalData,
					accountStatisticsData = statisticData,
				)
			}
		}
	}
}