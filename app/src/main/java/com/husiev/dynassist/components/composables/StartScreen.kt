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
	settingsViewModel: StartViewModel,
	modifier: Modifier = Modifier
) {
	val accounts by settingsViewModel.accounts.collectAsStateWithLifecycle()
	val searchResult by settingsViewModel.searchResult.collectAsStateWithLifecycle()
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
				accounts = searchResult,
				modifier = modifier,
				onChangeContent = { showStartScreen = true }
			)
	}
}