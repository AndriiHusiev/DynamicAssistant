package com.husiev.dynassist.components.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
	modifier: Modifier = Modifier,
	onSearch: (String) -> Unit = {}
) {
	var text by remember { mutableStateOf("") }
	val keyboardController = LocalSoftwareKeyboardController.current
	val focusManager = LocalFocusManager.current
	
	TextField(
		value = text,
		onValueChange = { text = it },
		modifier = modifier.fillMaxWidth(),
		placeholder = { Text(stringResource(R.string.search_text)) },
		leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
		keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
		keyboardActions = KeyboardActions(onSearch = {
			onSearch(text)
			// Hide the keyboard after submitting the search
			keyboardController?.hide()
			//or hide keyboard
			focusManager.clearFocus()
			
		}),
		colors = TextFieldDefaults.textFieldColors(
			backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.8f)
		),
	)
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
	DynamicAssistantTheme {
		SearchBar {}
	}
}