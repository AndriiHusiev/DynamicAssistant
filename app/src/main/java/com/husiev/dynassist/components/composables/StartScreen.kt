package com.husiev.dynassist.components.composables

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.components.StartViewModel

@Composable
fun StartScreen(
	modifier: Modifier = Modifier,
	theme: ThemeConfig = ThemeConfig.FOLLOW_SYSTEM,
	onChangeTheme: (themeConfig: ThemeConfig) -> Unit,
	searchViewModel: StartViewModel = hiltViewModel(),
) {
	val accounts by searchViewModel.accounts.collectAsStateWithLifecycle()
	val searchResult by searchViewModel.searchResult.collectAsStateWithLifecycle()
	val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
	var showStartScreen by rememberSaveable { mutableStateOf(true) }
	
	Crossfade(
		targetState = showStartScreen,
		label = "AnimStartContent",
	) {
		if (it)
			StartContent(
				accounts = accounts,
				modifier = modifier,
				theme = theme,
				onChangeTheme = onChangeTheme,
				onChangeContent = { showStartScreen = false }
			)
		else
			SearchContent(
				searchState = searchResult,
				modifier = modifier,
				searchQuery = searchQuery,
				onChangeContent = { showStartScreen = true },
				onSearchQueryChanged = searchViewModel::onSearchQueryChanged,
				onSearchTriggered = searchViewModel::onSearchTriggered,
			)
	}
}