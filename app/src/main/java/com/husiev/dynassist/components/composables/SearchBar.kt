package com.husiev.dynassist.components.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.husiev.dynassist.R
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
	modifier: Modifier = Modifier,
	searchQuery: String = "",
	onSearchQueryChanged: (String) -> Unit = {},
	onSearchTriggered: (String) -> Unit = {},
	onBackClick: () -> Unit = {},
) {
	val keyboardController = LocalSoftwareKeyboardController.current
	val focusManager = LocalFocusManager.current
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier.fillMaxWidth(),
	) {
		IconButton(onClick = { onBackClick() }) {
			Icon(
				imageVector = Icons.Filled.ArrowBack,
				contentDescription = stringResource(
					id = R.string.back,
				),
			)
		}
		
		TextField(
			value = searchQuery,
			onValueChange = {
				if (!it.contains("\n")) {
					onSearchQueryChanged(it)
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(dimensionResource(R.dimen.padding_big)),
			placeholder = { Text(stringResource(R.string.search_nickname_text)) },
			leadingIcon = {
				Icon(
					imageVector = Icons.Filled.Search,
					contentDescription = stringResource(R.string.description_search_text),
					tint = MaterialTheme.colors.onSurface
				)
			},
			trailingIcon = {
				if (searchQuery.isNotEmpty()) {
					IconButton(
						onClick = { onSearchQueryChanged("") },
					) {
						Icon(
							imageVector = Icons.Filled.Close,
							contentDescription = stringResource(
								id = R.string.description_clear_search_text,
							),
							tint = MaterialTheme.colors.onSurface,
						)
					}
				}
			},
			keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
			keyboardActions = KeyboardActions(onSearch = {
				onSearchTriggered(searchQuery)
				// Hide the keyboard after submitting the search
				keyboardController?.hide()
				//or hide keyboard
				focusManager.clearFocus()
				
			}),
			shape = RoundedCornerShape(32.dp),
			colors = TextFieldDefaults.textFieldColors(
				backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.8f),
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
				disabledIndicatorColor = Color.Transparent,
			),
			maxLines = 1,
			singleLine = true,
		)
	}
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
	DynamicAssistantTheme {
		SearchBar {}
	}
}