package com.husiev.dynassist.components.main.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.components.start.composables.DATopAppBar
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
	title: String,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		containerColor = Color.Transparent,
		contentColor = MaterialTheme.colorScheme.onBackground,
	) { innerPadding ->
		Column {
			DATopAppBar(
				modifier = Modifier.padding(innerPadding),
				title = title,
			)
			
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainContentPreview() {
	DynamicAssistantTheme {
		MainContent("Nickname")
	}
}