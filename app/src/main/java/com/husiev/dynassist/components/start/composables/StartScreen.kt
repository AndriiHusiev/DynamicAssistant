package com.husiev.dynassist.components.start.composables

import android.content.Context
import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.components.main.MainActivity
import com.husiev.dynassist.components.start.StartViewModel
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.network.SearchResultUiState

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
	val mContext = LocalContext.current
	
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
				onChangeContent = { showStartScreen = false },
				onSelectPlayer = { account -> startMainActivity(mContext, account) },
				onDeletePlayer = searchViewModel::deleteAccount
			)
		else
			SearchContent(
				searchState = searchResult,
				modifier = modifier,
				searchQuery = searchQuery,
				onChangeContent = {
					searchViewModel.onSearchQueryChanged("")
					searchViewModel.searchResult.value = SearchResultUiState.EmptyQuery
					showStartScreen = true
				},
				onSearchQueryChanged = searchViewModel::onSearchQueryChanged,
				onSearchTriggered = searchViewModel::onSearchTriggered,
				onSelectNickname = { startAccountInfo ->
					searchViewModel.addAccount(startAccountInfo)
					searchViewModel.onSearchQueryChanged("")
					searchViewModel.searchResult.value = SearchResultUiState.EmptyQuery
					showStartScreen = true
					startMainActivity(mContext, startAccountInfo)
				}
			)
	}
}

fun startMainActivity(context: Context, account: StartAccountInfo) {
	val intent = Intent(context, MainActivity::class.java).apply {
		putExtra("nickname", account.nickname)
		putExtra("account_id", account.id)
	}
	context.startActivity(intent)
}