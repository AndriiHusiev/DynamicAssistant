package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.ConvertedStatData
import com.husiev.dynassist.components.main.utils.convert
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun MainCard(
	title: String,
	items: Map<String, Any?>,
	divider: Float,
	modifier: Modifier = Modifier,
	onClick: (String) -> Unit = {},
) {
	ElevatedCard(
		modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium))),
		shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
		Text(
			text = title,
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
			style = MaterialTheme.typography.headlineSmall
		)
		
		for((text, value) in items) {
			Divider(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = dimensionResource(R.dimen.padding_small))
			)
			MainItem(
				text = text,
				data = value.convert(divider),
				onClick = onClick
			)
		}
	}
}

@Composable
fun MainItem(
	text: String,
	data: ConvertedStatData,
	modifier: Modifier = Modifier,
	onClick: (String) -> Unit = {},
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.clickable { onClick("") },
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = text,
			modifier = Modifier
				.padding(
					horizontal = dimensionResource(R.dimen.padding_medium),
					vertical = dimensionResource(R.dimen.padding_big),
				),
			style = MaterialTheme.typography.bodyLarge
		)
		
		Row(
			modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_small)),
			verticalAlignment = Alignment.CenterVertically,
		) {
			if (data.diffValue == null) {
				Text(
					text = data.currentValue,
					modifier = Modifier.padding(
						horizontal = dimensionResource(R.dimen.padding_medium),
						vertical = dimensionResource(R.dimen.padding_extra_small)
					),
					style = MaterialTheme.typography.bodyMedium
				)
			} else {
				Icon(
					imageVector = data.imageVector,
					contentDescription = null,
					tint = data.color
				)
				
				Column(
					modifier = Modifier
						.padding(vertical = dimensionResource(R.dimen.padding_extra_small)),
					horizontalAlignment = Alignment.End
				) {
					Text(
						text = data.currentValue,
						modifier = Modifier.padding(
							horizontal = dimensionResource(R.dimen.padding_medium),
							vertical = dimensionResource(R.dimen.padding_extra_small)
						),
						style = MaterialTheme.typography.bodyMedium
					)
					Text(
						text = data.diffValue,
						modifier = Modifier.padding(
							horizontal = dimensionResource(R.dimen.padding_medium),
							vertical = dimensionResource(R.dimen.padding_extra_small)
						),
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
			
			Icon(
				imageVector = Icons.Filled.ChevronRight,
				contentDescription = null
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun MainCardPreview() {
	DynamicAssistantTheme {
		MainCard(
			title = "Overall results",
			items = mapOf<String, Any?>(
				"Battles" to 11785,
				"Victories" to 6781,
				"Defeats" to 4944,
				"Draws" to 60,
				"Float" to 5.2f,
				"Nullable" to null
			),
			divider = 11785f
		)
	}
}