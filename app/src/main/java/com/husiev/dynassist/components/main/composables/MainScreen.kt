package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.husiev.dynassist.components.main.MainViewModel
import com.husiev.dynassist.components.main.navigation.DaNavHost
import com.husiev.dynassist.components.main.utils.DaAppState
import com.husiev.dynassist.components.main.utils.rememberDaAppState
import com.husiev.dynassist.components.start.composables.DaTopAppBar
import com.husiev.dynassist.components.start.utils.StartAccountInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	account: StartAccountInfo,
	modifier: Modifier = Modifier,
	mainViewModel: MainViewModel = hiltViewModel(),
	appState: DaAppState = rememberDaAppState(),
) {
	
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
		Column {
			DaTopAppBar(
				modifier = Modifier.padding(innerPadding),
				title = account.nickname,
			)
			
			DaNavHost(appState = appState)
		}
	}
}