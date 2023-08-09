package com.husiev.dynassist.components.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.utils.StartAccountInfo
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun StartTopAppBar(
	title: String,
	modifier: Modifier = Modifier,
) {
	TopAppBar(
		title = { Text(
			text = title,
			color = MaterialTheme.colorScheme.onPrimary,
			style = MaterialTheme.typography.titleLarge
		) },
		modifier = modifier,
		backgroundColor = MaterialTheme.colorScheme.primary,
	)
}

@Composable
fun StartFAB(
	title: String,
	modifier: Modifier = Modifier,
	image: ImageVector = Icons.Default.Add,
	onClick: () -> Unit,
) {
	ExtendedFloatingActionButton(
		text = { Text(text = title) },
		icon = {
			Icon(
				imageVector = image,
				contentDescription = stringResource(R.string.description_start_fab),
			)
		},
		onClick = onClick,
		modifier = modifier,
		expanded = title != "",
	)
}

@Composable
fun StartContent(
	accounts: List<StartAccountInfo>,
	modifier: Modifier = Modifier,
	onChangeContent: () -> Unit = {}
) {
	val state = rememberLazyListState()
	
	Scaffold(
		modifier = modifier,
		topBar = {
			StartTopAppBar(title = stringResource(R.string.app_name))
		},
		floatingActionButton = {
			StartFAB(title = stringResource(R.string.start_fab_title)) {
				onChangeContent()
			}
		}
	) { innerPadding ->
		LazyColumn(
			state = state,
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.secondaryContainer),
		) {
			items(accounts) {account ->
				AccountListItem(
					account = account
				)
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
		StartContent(listOf(
			StartAccountInfo("load","DTS"), StartAccountInfo("vector"),
			StartAccountInfo("asset"), StartAccountInfo("format","KFC"),
			StartAccountInfo("MaterialThemeColorSchemeOnSecondaryContainer","BTW"),
//			StartAccountInfo("modifier"), StartAccountInfo("You"),
//			StartAccountInfo("png"), StartAccountInfo("logo"),
//			StartAccountInfo("element"), StartAccountInfo("content","NIL"),
			)
		)
	}
}