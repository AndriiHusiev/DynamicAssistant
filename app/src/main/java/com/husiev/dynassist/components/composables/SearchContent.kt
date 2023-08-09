package com.husiev.dynassist.components.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SearchTopAppBar(
	title: String,
	modifier: Modifier = Modifier,
	canNavigateBack: Boolean = false,
	onClose: () -> Unit = {},
) {
	TopAppBar(
		title = { Text(
			text = title,
			color = MaterialTheme.colorScheme.onPrimary,
			style = MaterialTheme.typography.titleLarge
		) },
		modifier = modifier,
		navigationIcon = {
			if (canNavigateBack) {
				IconButton(onClick = onClose) {
					Icon(
						imageVector = Icons.Filled.ArrowBack,
						contentDescription = null,
						tint = MaterialTheme.colorScheme.onPrimary
					)
				}
			}
		},
		backgroundColor = MaterialTheme.colorScheme.primary,
	)
}

@Composable
fun SearchContent(
	accounts: List<String>,
	modifier: Modifier = Modifier,
	onChangeContent: () -> Unit = {}
) {
	val state = rememberLazyListState()
	
	Scaffold(
		modifier = modifier,
		topBar = {
			SearchTopAppBar(
				title = stringResource(R.string.search_header),
				canNavigateBack = true,
				onClose = onChangeContent
			)
		},
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.secondaryContainer),
		) {
			Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
			SearchBar(
				Modifier.padding(horizontal = dimensionResource(R.dimen.padding_big)),
				onSearch = {}
			)
			Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
			
			Divider(color = MaterialTheme.colorScheme.outline)
			LazyColumn(
				Modifier.padding(horizontal = dimensionResource(R.dimen.padding_big)),
				state = state,
			) {
				items(accounts) {
					SearchListItem(
						text = it,
						bgColor = MaterialTheme.colorScheme.secondaryContainer,
						textColor = MaterialTheme.colorScheme.onSecondaryContainer,
						dividerColor = MaterialTheme.colorScheme.outlineVariant,
					)
				}
			}
		}
	}
	
	BackHandler(
		enabled = true,
		onBack = onChangeContent
	)
}

@Preview(showBackground = true)
@Composable
fun SearchContentPreview() {
	DynamicAssistantTheme {
		SearchContent(listOf(
			"load","DTS","vector","asset","format","KFC","png","logo","element","content",
			"MaterialThemeColorSchemeOnSecondaryContainer","BTW","modifier","You")
		)
	}
}