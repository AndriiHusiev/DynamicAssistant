package com.husiev.dynassist.components.main.composables

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SummaryCardItem(
	title: String,
	mainValue: String,
	auxValue: String?,
	absSessionValue: String?,
	color: Color?,
	imageVector: ImageVector?,
	modifier: Modifier = Modifier,
	onClick: (String) -> Unit = {},
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.clickable { onClick(title) },
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = title,
			modifier = Modifier
				.padding(
					horizontal = dimensionResource(R.dimen.padding_medium),
					vertical = dimensionResource(R.dimen.padding_large),
				),
			style = MaterialTheme.typography.bodyLarge
		)
		
		Row(
			modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_extra_small)),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Column(
				modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_extra_small)),
				horizontalAlignment = Alignment.End
			) {
				Text(
					text = mainValue,
					style = MaterialTheme.typography.bodyLarge
				)
				
				Row(verticalAlignment = Alignment.CenterVertically) {
					if (auxValue != null && imageVector != null && color != null) {
						Icon(
							imageVector = imageVector,
							contentDescription = null,
							tint = color
						)
					}
					
					Text(
						text = auxValue ?: (absSessionValue ?: "--"),
						style = MaterialTheme.typography.bodySmall
					)
					
				}
			}
			
//			auxValue?.let {
				Icon(
					imageVector = Icons.Filled.ChevronRight,
					contentDescription = null
				)
//			}
		}
	}
}

@Composable
fun MainDivider() {
	Divider(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = dimensionResource(R.dimen.padding_small)),
		color = MaterialTheme.colorScheme.outline
	)
}

@Composable
fun MainCardVehicle(
	title: String,
	value: String,
	vehicleName: String,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.padding(
			horizontal = dimensionResource(R.dimen.padding_medium),
			vertical = dimensionResource(R.dimen.padding_medium),
		),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = dimensionResource(R.dimen.padding_extra_small)),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.bodyLarge
			)
			
			Text(
				text = value,
				style = MaterialTheme.typography.bodyLarge
			)
		}
		
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = dimensionResource(R.dimen.padding_extra_small)),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.achieved_at),
				modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)),
				style = MaterialTheme.typography.bodyMedium
			)
			
			Text(
				text = vehicleName,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainCardItemPreview() {
	DynamicAssistantTheme {
		Surface(
			color = MaterialTheme.colorScheme.background
		) {
			Column {
				SummaryCardItem(
					title = "Battles",
					mainValue = "254",
					auxValue = null,
					absSessionValue = "+5",
					color = null,
					imageVector = null,
				)
				MainDivider()
				SummaryCardItem(
					title = "Victories",
					mainValue = "56.3%",
					auxValue = "+0.024% / 58.2%",
					absSessionValue = "+5",
					color = Color.Green,
					imageVector = Icons.Filled.ArrowDropUp,
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun MainCardVehiclePreview() {
	DynamicAssistantTheme {
		MainCardVehicle(
			title = "Maximum experience",
			value = "2708",
			vehicleName = "T-34",
		)
	}
}
