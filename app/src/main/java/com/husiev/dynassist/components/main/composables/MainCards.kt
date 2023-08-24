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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.happyIcon
import com.husiev.dynassist.components.main.utils.happyColor
import com.husiev.dynassist.components.main.utils.toScreen
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun MainCardOverall(
	summaryData: List<AccountStatisticsData>,
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
			text = stringResource(R.string.summary_header_overall),
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
			style = MaterialTheme.typography.headlineSmall
		)
		
		var item = summaryData.first { it.tag == "battles" }
		MainDivider()
		MainItem(
			title = item.title,
			baseValue = item.absValue,
			progressValue = item.absSessionValue,
			sessionValue = null,
			multiplier = 1f,
			showOnlyProgress = true,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "wins" }
		MainDivider()
		MainItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			suffix = "%",
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "losses" }
		MainDivider()
		MainItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			revertHappiness = true,
			suffix = "%",
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "draws" }
		MainDivider()
		MainItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			revertHappiness = true,
			suffix = "%",
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "frags" }
		MainDivider()
		MainItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "xp" }
		MainDivider()
		MainItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "survivedBattles" }
		MainDivider()
		MainItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			suffix = "%",
			onClick = onClick
		)
	}
}

@Composable
fun MainItem(
	title: String,
	baseValue: Float?,
	progressValue: Float?,
	sessionValue: Float?,
	modifier: Modifier = Modifier,
	revertHappiness: Boolean = false,
	suffix: String = "",
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
					vertical = dimensionResource(R.dimen.padding_big),
				),
			style = MaterialTheme.typography.bodyLarge
		)
		
		Row(
			modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_small)),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Column(
				modifier = Modifier
					.padding(vertical = dimensionResource(R.dimen.padding_extra_small)),
				horizontalAlignment = Alignment.End
			) {
				Text(
					text = baseValue.toScreen(multiplier, suffix, false, forceToInt),
					modifier = Modifier.padding(
						horizontal = dimensionResource(R.dimen.padding_medium),
						vertical = dimensionResource(R.dimen.padding_extra_small)
					),
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
							modifier = Modifier.padding(
								end = dimensionResource(R.dimen.padding_medium),
								top = dimensionResource(R.dimen.padding_extra_small),
								bottom = dimensionResource(R.dimen.padding_extra_small)
							),
							style = MaterialTheme.typography.bodySmall
						)
						
					}
				}
			}
			
			Icon(
				imageVector = Icons.Filled.ChevronRight,
				contentDescription = null
			)
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

@Preview(showBackground = true)
@Composable
fun MainCardPreview() {
	DynamicAssistantTheme {
		Column {
			MainItem(
				title = "Battles",
				baseValue = 251f,
				progressValue = 3f,
				sessionValue = null,
				multiplier = 1f,
				forceToInt = true,
				showOnlyProgress = true,
			)
			MainDivider()
			MainItem(
				title = "Victories",
				baseValue = 0.467f,
				progressValue = 0.000548f,
				sessionValue = 0.5216f,
				suffix = "%",
			)
			MainDivider()
			MainItem(
				title = "Defeats",
				baseValue = 0.467f,
				progressValue = 0.000548f,
				sessionValue = 0.5216f,
				revertHappiness = true,
				suffix = "%",
			)
			MainDivider()
			MainItem(
				title = "Experience",
				baseValue = 789.798f,
				progressValue = -0.00548345f,
				sessionValue = 0.6258f,
				multiplier = 1f,
			)
			MainDivider()
			MainItem(
				title = "Frags",
				baseValue = 1.738f,
				progressValue = null,
				sessionValue = null,
				multiplier = 1f,
			)
			MainDivider()
			MainItem(
				title = "Battles survived",
				baseValue = null,
				progressValue = null,
				sessionValue = null,
				suffix = "%",
			)
			MainDivider()
			MainItem(
				title = "Blocked damage",
				baseValue = 288.75f,
				progressValue = null,
				sessionValue = null,
				showSecondaryData = false,
				multiplier = 1f,
			)
		}
	}
}