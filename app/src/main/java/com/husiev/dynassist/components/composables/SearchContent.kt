package com.husiev.dynassist.components.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
		topBar = {
			SearchTopAppBar(
				title = stringResource(R.string.search_header),
				canNavigateBack = true,
				onClose = onChangeContent
			)
		},
	) { innerPadding ->
		SearchBar(
			onSearch = {}
		)
		
		LazyColumn(
			state = state,
			modifier = modifier
				.padding(innerPadding),
		) {
			items(accounts) {
				SearchListItem(
					text = it
				)
			}
		}
	}
	
	BackHandler(
		enabled = true,
		onBack = onChangeContent
	)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
	var text by remember { mutableStateOf("") }
	val keyboardController = LocalSoftwareKeyboardController.current
	val focusManager = LocalFocusManager.current
	
	
	TextField(
		value = text,
		onValueChange = { text = it },
		label = { Text("Search") },
		leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
		modifier = Modifier.fillMaxWidth(),
		keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
		keyboardActions = KeyboardActions(onSearch = {
			onSearch(text)
			// Hide the keyboard after submitting the search
			keyboardController?.hide()
			//or hide keyboard
			focusManager.clearFocus()
			
		})
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