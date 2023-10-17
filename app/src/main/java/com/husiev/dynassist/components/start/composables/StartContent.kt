package com.husiev.dynassist.components.start.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartContent(
	accounts: List<StartAccountInfo>,
	modifier: Modifier = Modifier,
	theme: ThemeConfig = ThemeConfig.FOLLOW_SYSTEM,
	onChangeTheme: (themeConfig: ThemeConfig) -> Unit = {},
	onChangeContent: () -> Unit = {},
	onSelectPlayer: (StartAccountInfo) -> Unit = {},
	onDeletePlayer: (StartAccountInfo) -> Unit = {},
) {
	val state = rememberLazyListState()
	var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
	if (showSettingsDialog) {
		SettingsDialog(
			theme = theme,
			onDismiss = { showSettingsDialog = false },
			onChangeTheme = onChangeTheme
		)
	}
	
	Scaffold(
		modifier = modifier,
		containerColor = Color.Transparent,
		contentColor = MaterialTheme.colorScheme.onBackground,
	) { innerPadding ->
		Column {
			DaTopAppBar(
				title = stringResource(R.string.app_name),
				navigationIcon = Icons.Filled.Search,
				navigationIconContentDescription = stringResource(R.string.description_search_text),
				actionIcon = Icons.Filled.Settings,
				actionIconContentDescription = stringResource(R.string.description_settings_text),
				onActionClick = { showSettingsDialog = true },
				onNavigationClick = onChangeContent,
			)
			LazyColumn(
				state = state,
				modifier = Modifier
					.padding(innerPadding)
					.fillMaxSize()
			) {
				items(accounts) {account ->
					AccountListItem(
						account = account,
						onClick = onSelectPlayer,
						onDelete = onDeletePlayer
					)
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun StartContentPreview() {
	DynamicAssistantTheme {
		val dateTime = Date().time.toString()
		StartContent(listOf(
			StartAccountInfo(1, "load","DTS", null, dateTime),
			StartAccountInfo(1, "vector", updateTime = dateTime),
			StartAccountInfo(1, "asset", updateTime = dateTime),
			StartAccountInfo(1, "format","KFC", null, dateTime),
			StartAccountInfo(
				id = 1,
				nickname = "MaterialThemeColorSchemeOnSecondaryContainer",
				clan = "BTW",
				updateTime = dateTime,
				notification = NotifyEnum.UPDATES_AVAIL),
			)
		)
	}
}