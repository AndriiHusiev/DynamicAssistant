package com.husiev.dynassist.components.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SearchListItem(
	text: String,
	modifier: Modifier = Modifier,
	bgColor: Color = MaterialTheme.colorScheme.secondaryContainer,
	textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
	dividerColor: Color = MaterialTheme.colorScheme.outlineVariant,
	onClick: () -> Unit = {}
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.background(color = bgColor)
			.clickable(onClick = onClick),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = text,
				modifier = Modifier
					.padding(
						horizontal = dimensionResource(R.dimen.padding_large),
						vertical = dimensionResource(R.dimen.padding_small)
					),
				color = textColor,
				overflow = TextOverflow.Ellipsis,
				maxLines = 1,
			)
		}
		Divider(
			modifier = Modifier.padding(
				horizontal = dimensionResource(R.dimen.padding_medium)
			),
			color = dividerColor
		)
	}
}

@Preview(showBackground = true)
@Composable
fun SearchListItemPreview() {
	DynamicAssistantTheme {
		SearchListItem(
			text = "qwerty"
		)
	}
}