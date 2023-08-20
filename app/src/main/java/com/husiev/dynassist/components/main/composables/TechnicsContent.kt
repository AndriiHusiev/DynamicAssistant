package com.husiev.dynassist.components.main.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun TechnicsContent(
	modifier: Modifier = Modifier,
	title: String = "",
	onClick: (String) -> Unit = {}
) {
	val state = rememberLazyListState()
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		item { Text(text = "TechnicsContent") }
		item { Text(text = "in progress...") }
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TechnicsContentPreview() {
	DynamicAssistantTheme {
		TechnicsContent()
	}
}
