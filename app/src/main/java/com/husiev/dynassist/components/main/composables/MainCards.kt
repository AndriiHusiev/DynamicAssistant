package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.AccountStatisticsData

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
		MainCardItem(
			title = item.title,
			baseValue = item.absValue,
			progressValue = item.absSessionValue,
			sessionValue = null,
			multiplier = 1f,
			forceToInt = true,
			showOnlyProgress = true,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "wins" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			suffix = "%",
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "losses" }
		MainDivider()
		MainCardItem(
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
		MainCardItem(
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
		MainCardItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "xp" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "survivedBattles" }
		MainDivider()
		MainCardItem(
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
fun MainCardPerformance(
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
			text = stringResource(R.string.summary_header_performance),
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
			style = MaterialTheme.typography.headlineSmall
		)
		
		var item = summaryData.first { it.tag == "spotted" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "capturePoints" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "droppedCapturePoints" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "damageDealt" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			onClick = onClick
		)
		
		item = summaryData.first { it.tag == "damageReceived" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.avgValue,
			progressValue = item.avgSessionProgress,
			sessionValue = item.avgSessionValue,
			multiplier = 1f,
			revertHappiness = true,
			onClick = onClick
		)
	}
}

@Composable
fun MainCardRecord(
	summaryData: List<AccountStatisticsData>,
	modifier: Modifier = Modifier,
) {
	ElevatedCard(
		modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium))),
		shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
		Text(
			text = stringResource(R.string.summary_header_record),
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
			style = MaterialTheme.typography.headlineSmall
		)
		
		var item = summaryData.first { it.tag == "maxXp" }
		var itemAux = summaryData.first { it.tag == "maxXpTankId" }
		MainDivider()
		MainCardVehicle(
			title = item.title,
			value = item.absValue,
			auxTitle = itemAux.title,
			vehicleId = itemAux.absValue,
		)
		
		item = summaryData.first { it.tag == "maxDamage" }
		itemAux = summaryData.first { it.tag == "maxDamageTankId" }
		MainDivider()
		MainCardVehicle(
			title = item.title,
			value = item.absValue,
			auxTitle = itemAux.title,
			vehicleId = itemAux.absValue,
		)
		
		item = summaryData.first { it.tag == "maxFrags" }
		itemAux = summaryData.first { it.tag == "maxFragsTankId" }
		MainDivider()
		MainCardVehicle(
			title = item.title,
			value = item.absValue,
			auxTitle = itemAux.title,
			vehicleId = itemAux.absValue,
		)
	}
}

@Composable
fun MainCardAverage(
	summaryData: List<AccountStatisticsData>,
	modifier: Modifier = Modifier,
) {
	ElevatedCard(
		modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium))),
		shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
		Text(
			text = stringResource(R.string.summary_header_average),
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
			style = MaterialTheme.typography.headlineSmall
		)
		
		var item = summaryData.first { it.tag == "avgDamageBlocked" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.absValue,
			progressValue = null,
			sessionValue = null,
			showArrow = false,
			showSecondaryData = false,
			multiplier = 1f,
		)
		
		item = summaryData.first { it.tag == "avgDamageAssisted" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.absValue,
			progressValue = null,
			sessionValue = null,
			showArrow = false,
			showSecondaryData = false,
			multiplier = 1f,
		)
		
		item = summaryData.first { it.tag == "avgDamageAssistedTrack" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.absValue,
			progressValue = null,
			sessionValue = null,
			showArrow = false,
			showSecondaryData = false,
			multiplier = 1f,
		)
		
		item = summaryData.first { it.tag == "avgDamageAssistedRadio" }
		MainDivider()
		MainCardItem(
			title = item.title,
			baseValue = item.absValue,
			progressValue = null,
			sessionValue = null,
			showArrow = false,
			showSecondaryData = false,
			multiplier = 1f,
		)
	}
}