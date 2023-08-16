package com.husiev.dynassist.components.main.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.husiev.dynassist.components.main.MainViewModel
import com.husiev.dynassist.components.start.utils.StartAccountInfo

@Composable
fun MainScreen(
	account: StartAccountInfo,
	modifier: Modifier = Modifier,
	mainViewModel: MainViewModel = hiltViewModel(),
) {
	MainContent(
		title = account.nickname,
		modifier = modifier
	)
}