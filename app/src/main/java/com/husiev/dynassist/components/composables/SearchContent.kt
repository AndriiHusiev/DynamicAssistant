package com.husiev.dynassist.components.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.husiev.dynassist.R
import com.husiev.dynassist.network.SearchResultUiState

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
	searchState: SearchResultUiState,
	modifier: Modifier = Modifier,
	onChangeContent: () -> Unit = {},
	searchQuery: String = "",
	onSearchQueryChanged: (String) -> Unit = {},
	onSearchTriggered: (String) -> Unit = {}
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
				modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_big)),
				searchQuery = searchQuery,
				onSearchQueryChanged = onSearchQueryChanged,
				onSearchTriggered = onSearchTriggered,
			)
			Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
			
			Divider(color = MaterialTheme.colorScheme.outline)
			
			when (searchState) {
				SearchResultUiState.EmptyQuery,
				SearchResultUiState.LoadFailed -> ErrorScreen(modifier = Modifier.fillMaxSize())
				
				SearchResultUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
				
				is SearchResultUiState.Success -> {
					if (searchState.isEmpty()) {
						EmptySearchResultBody(searchQuery)
					} else if (searchState.accounts.status == "ok") {
						LazyColumn(
							Modifier.padding(horizontal = dimensionResource(R.dimen.padding_big)),
							state = state,
						) {
							searchState.accounts.data?.let {
								items(it.map { account -> account.nickname }) { nickname ->
									SearchListItem(
										text = nickname,
										bgColor = MaterialTheme.colorScheme.secondaryContainer,
										textColor = MaterialTheme.colorScheme.onSecondaryContainer,
										dividerColor = MaterialTheme.colorScheme.outlineVariant,
									)
								}
							}
						}
					} else {
						Text(searchState.accounts.status)
						searchState.accounts.error?.let {
							searchState.accounts.error.field?.let { txt -> Text("field: $txt") }
							Text("message: ${searchState.accounts.error.message}")
							Text("code: ${searchState.accounts.error.code}")
							Text("value: ${searchState.accounts.error.value}")
						}
					}
				}
			}
		}
	}
	
	BackHandler(
		enabled = true,
		onBack = onChangeContent
	)
}

@Composable
fun EmptySearchResultBody(
	searchQuery: String,
) {
	val message = stringResource(id = R.string.search_result_not_found, searchQuery)
	val start = message.indexOf(searchQuery)
	Text(
		text = AnnotatedString(
			text = message,
			spanStyles = listOf(
				AnnotatedString.Range(
					SpanStyle(fontWeight = FontWeight.Bold),
					start = start,
					end = start + searchQuery.length,
				),
			),
		),
		style = MaterialTheme.typography.bodyLarge,
		textAlign = TextAlign.Center,
		modifier = Modifier.padding(vertical = 24.dp),
	)
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
	Image(
		modifier = modifier.size(200.dp),
		painter = painterResource(R.drawable.loading_img),
		contentDescription = stringResource(R.string.app_name)//loading)
	)
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(R.drawable.loading_img),//ic_connection_error),
			contentDescription = ""
		)
		Text(
			text = stringResource(R.string.app_name),//loading_failed),
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
		)
	}
}