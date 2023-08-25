package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.happyColor
import com.husiev.dynassist.components.main.utils.happyIcon
import com.husiev.dynassist.components.main.utils.toScreen
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun MainCardItem(
	title: String,
	baseValue: Float?,
	progressValue: Float?,
	sessionValue: Float?,
	modifier: Modifier = Modifier,
	revertHappiness: Boolean = false,
	suffix: String = "",
	showArrow: Boolean = true,
	showSecondaryData: Boolean = true,
	showOnlyProgress: Boolean = false,
	forceToInt: Boolean = false,
	multiplier: Float = 100f,
	onClick: (String) -> Unit = {},
) {
	val secondaryText = progressValue.toScreen(multiplier, suffix, true, forceToInt) +
			if (showOnlyProgress)
				""
			else
				" / " + sessionValue.toScreen(multiplier, suffix)
	
	Row(
		modifier = modifier
			.fillMaxWidth()
			.clickable { onClick("") },
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
					text = baseValue.toScreen(multiplier, suffix, false, forceToInt),
					style = MaterialTheme.typography.bodyLarge
				)
				
				if (showSecondaryData) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						if (progressValue != null && sessionValue != null) {
							Icon(
								imageVector = progressValue.happyIcon(),
								contentDescription = null,
								tint = progressValue.happyColor(revertHappiness)
							)
						}
						
						Text(
							text = secondaryText,
							style = MaterialTheme.typography.bodySmall
						)
						
					}
				}
			}
			
			if (showArrow) {
				Icon(
					imageVector = Icons.Filled.ChevronRight,
					contentDescription = null
				)
			}
		}
	}
}

@Composable
fun MainDivider() {
	Divider(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = dimensionResource(R.dimen.padding_small))
	)
}

@Composable
fun MainCardVehicle(
	title: String,
	value: Float?,
	auxTitle: String,
	vehicleId: Float?,
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
				text = value.toScreen(1f, forceToInt = true),
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
				text = auxTitle,
				style = MaterialTheme.typography.bodyLarge
			)
			
			Text(
				text = vehicleId.toScreen(1f, forceToInt = true),
				style = MaterialTheme.typography.bodyLarge
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun MainCardVehiclePreview() {
	DynamicAssistantTheme {
		MainCardVehicle(
			title = "Maximum experience",
			value = 2708f,
			auxTitle = "Max. exp. achieved at",
			vehicleId = 251f,
		)
	}
}

@Preview(showBackground = true)
@Composable
fun MainCardPreview() {
	DynamicAssistantTheme {
		Column {
			MainCardItem(
				title = "Battles",
				baseValue = 251f,
				progressValue = 3f,
				sessionValue = null,
				multiplier = 1f,
				forceToInt = true,
				showOnlyProgress = true,
			)
			MainDivider()
			MainCardItem(
				title = "Victories",
				baseValue = 0.467f,
				progressValue = 0.000548f,
				sessionValue = 0.5216f,
				suffix = "%",
			)
			MainDivider()
			MainCardItem(
				title = "Defeats",
				baseValue = 0.467f,
				progressValue = 0.000548f,
				sessionValue = 0.5216f,
				revertHappiness = true,
				suffix = "%",
			)
			MainDivider()
			MainCardItem(
				title = "Experience",
				baseValue = 789.798f,
				progressValue = -0.00548345f,
				sessionValue = 0.6258f,
				multiplier = 1f,
			)
			MainDivider()
			MainCardItem(
				title = "Frags",
				baseValue = 1.738f,
				progressValue = null,
				sessionValue = null,
				multiplier = 1f,
			)
			MainDivider()
			MainCardItem(
				title = "Battles survived",
				baseValue = null,
				progressValue = null,
				sessionValue = null,
				suffix = "%",
			)
			MainDivider()
			MainCardItem(
				title = "Blocked damage",
				baseValue = 288.75f,
				progressValue = null,
				sessionValue = null,
				showArrow = false,
				showSecondaryData = false,
				multiplier = 1f,
			)
		}
	}
}
