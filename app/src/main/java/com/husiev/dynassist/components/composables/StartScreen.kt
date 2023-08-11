package com.husiev.dynassist.components.composables

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.components.StartViewModel

@Composable
fun StartScreen(
	searchViewModel: StartViewModel,
	modifier: Modifier = Modifier
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