package com.husiev.dynassist.components.start.composables

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.network.dataclasses.NetworkAccountInfo
import com.husiev.dynassist.network.dataclasses.NetworkErrorInfo
import com.husiev.dynassist.network.SearchResultUiState
import com.husiev.dynassist.network.dataclasses.NetworkStartSearchInfo
import com.husiev.dynassist.network.dataclasses.asExternalModel
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SearchContent(
	modifier: Modifier = Modifier,
	searchState: SearchResultUiState = SearchResultUiState.EmptyQuery,
	onChangeContent: () -> Unit = {},
	searchQuery: String = "",
	onSearchQueryChanged: (String) -> Unit = {},
	onSearchTriggered: (String) -> Unit = {},
	onSelectNickname: (StartAccountInfo) -> Unit = {}
) {
	val state = rememberLazyListState()
	
	Column(
		modifier = modifier
			.fillMaxSize()
	) {
		SearchBar(
			searchQuery = searchQuery,
			onSearchQueryChanged = onSearchQueryChanged,
			onSearchTriggered = onSearchTriggered,
			onBackClick = onChangeContent,
		)
		
		when (searchState) {
			SearchResultUiState.EmptyQuery -> Unit
			
			SearchResultUiState.LoadFailed -> FailScreen(modifier = Modifier.fillMaxSize())
			
			SearchResultUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
			
			is SearchResultUiState.Success -> {
				if (searchState.isEmpty()) {
					EmptySearchResultBody(searchQuery)
				} else if (searchState.accounts.status == "ok") {
					LazyColumn(
						state = state,
					) {
						searchState.accounts.data?.let {
							items(it) { account ->
								SearchListItem(
									accountInfo = account.asExternalModel(),
									onClick = onSelectNickname
								)
							}
						}
					}
				} else {
					ErrorScreen(
						errorInfo = searchState.accounts,
						modifier = Modifier.fillMaxSize()
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
		modifier = Modifier.padding(
			horizontal = dimensionResource(R.dimen.padding_extra_large),
			vertical = dimensionResource(R.dimen.padding_large)
		),
	)
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
	val infiniteTransition = rememberInfiniteTransition(label = "rotation")
	val rotationAnim by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 360f,
		animationSpec = infiniteRepeatable(
			animation = tween(3000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "rotationAnimation"
	)
	
	Image(
		painter = painterResource(R.drawable.loading_img),
		contentDescription = stringResource(R.string.description_loading_content),
		contentScale = ContentScale.Crop,
		modifier = modifier
			.graphicsLayer {
				rotationZ = rotationAnim
			}
	)
}

@Composable
fun FailScreen(modifier: Modifier = Modifier) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(R.drawable.ic_connection_error),
			contentDescription = stringResource(R.string.description_loading_failed)
		)
		Text(
			text = stringResource(R.string.search_loading_failed),
			modifier = Modifier.padding(
				horizontal = dimensionResource(R.dimen.padding_extra_large),
				vertical = dimensionResource(R.dimen.padding_big)
			),
			textAlign = TextAlign.Center
		)
	}
}

@Composable
fun ErrorScreen(
	errorInfo: NetworkStartSearchInfo,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Column(
			modifier = Modifier
				.padding(horizontal = dimensionResource(R.dimen.padding_extra_large))
				.fillMaxWidth()
		) {
			Image(
				modifier = Modifier.fillMaxWidth(),
				painter = painterResource(R.drawable.ic_loading_error),
				contentDescription = stringResource(R.string.description_loading_failed),
				alignment = Alignment.Center
			)
			
			Text(
				text = stringResource(R.string.search_loading_error),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = dimensionResource(R.dimen.padding_extra_large),
						vertical = dimensionResource(R.dimen.padding_big)
					),
				textAlign = TextAlign.Center
			)
			Text("status: ${errorInfo.status}")
			errorInfo.error?.let {
				errorInfo.error.field?.let { txt -> Text("field: $txt") }
				Text("message: ${errorInfo.error.message}")
				Text("code: ${errorInfo.error.code}")
				Text("value: ${errorInfo.error.value}")
			}
		}
	}
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun SearchContentPreview() {
	DynamicAssistantTheme {
		SearchContent(searchState = SearchResultUiState.Success(
			NetworkStartSearchInfo(
				status = "ok",
				data = listOf(
					NetworkAccountInfo(1, "qwe"),
					NetworkAccountInfo(1, "rty"),
					NetworkAccountInfo(1, "asd"),
					NetworkAccountInfo(1, "fgh"),
				)
			)
		))
	}
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Composable
fun LoadingScreenPreview() {
	DynamicAssistantTheme {
		LoadingScreen(modifier = Modifier.fillMaxSize())
	}
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Composable
fun EmptySearchResultBodyPreview() {
	DynamicAssistantTheme {
		EmptySearchResultBody("asdf")
	}
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Composable
fun FailScreenScreenPreview() {
	DynamicAssistantTheme {
		FailScreen(modifier = Modifier.fillMaxSize())
	}
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Composable
fun ErrorScreenPreview() {
	DynamicAssistantTheme {
		ErrorScreen(
			NetworkStartSearchInfo(
				status = "error",
				error = NetworkErrorInfo(
					field = "search",
					message = "NOT_ENOUGH_BEER",
					code = 407,
					value = "2L"
				)
			),
			modifier = Modifier.fillMaxSize()
		)
	}
}