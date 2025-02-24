package com.husiev.dynassist.components.main.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.VehicleData
import com.husiev.dynassist.components.main.utils.bigToString
import com.husiev.dynassist.components.main.utils.flagToResId
import com.husiev.dynassist.components.main.utils.getMainAvg
import com.husiev.dynassist.components.main.utils.masteryToResId
import com.husiev.dynassist.components.main.utils.roleResId
import com.husiev.dynassist.components.main.utils.toScreen
import com.husiev.dynassist.database.entity.asStringDate
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SingleTechnicsContent(
	shortData: List<VehicleData>,
	modifier: Modifier = Modifier,
	singleId: Int? = null,
) {
	val state = rememberLazyListState()
	val singleItem = shortData.singleOrNull { it.tankId == singleId }
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		singleItem?.let { item ->
			item {
				SingleTechnicsImageCard(
					urlIcon = item.urlBigIcon,
					nation = item.nation,
					description = item.description,
					role = item.type,
					tier = item.tier,
					priceCredit = item.priceCredit,
					priceGold = item.priceGold,
				)
			}
			item {
				SingleTechnicsCard(item = item)
			}
		}
	}
}

@Composable
fun SingleTechnicsImageCard(
	urlIcon: String?,
	nation: String?,
	description: String?,
	role: String?,
	tier: Int?,
	priceCredit: Int?,
	priceGold: Int?,
	modifier: Modifier = Modifier,
) {
	var expanded by rememberSaveable { mutableStateOf(false) }
	
	ElevatedCard(
		modifier = modifier
			.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium)))
			.clickable(onClick = { expanded = !expanded })
			.animateContentSize(
				animationSpec = spring(
					dampingRatio = Spring.DampingRatioMediumBouncy,
					stiffness = Spring.StiffnessLow
				)
			),
		shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.general_info),
				modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
				style = MaterialTheme.typography.headlineSmall
			)
			IconButton(onClick = { expanded = !expanded }) {
				Icon(
					imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
					contentDescription = if (expanded)
						stringResource(R.string.show_less)
					else
						stringResource(R.string.show_more)
				)
			}
		}
		
		if (expanded) {
			Box(modifier = Modifier) {
				Image(
					painter = painterResource(flagToResId(nation)),
					contentDescription = null,
					modifier = Modifier.size(347.dp, 127.dp)
				)
				AsyncImage(
					model = urlIcon,
					error = painterResource(R.drawable.ic_tank_empty),
					placeholder = painterResource(R.drawable.ic_tank_empty),
					contentDescription = null,
					modifier = Modifier
						.fillMaxWidth()
						.size(200.dp, 127.dp)
				)
			}
			
			Text(
				text = description ?: "",
				modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
				style = MaterialTheme.typography.bodySmall
			)
			
			MainDivider()
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_role),
				value = stringResource(role.roleResId())
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_tier),
				value = tier?.toString() ?: NO_DATA
			)
			
			val textPrice = if (priceCredit == null && priceGold == null)
				stringResource(R.string.vehicle_not_for_sale)
			else {
				if (priceCredit == 0)
					priceGold.bigToString() + " " + stringResource(R.string.vehicle_price_gold)
				else if (priceGold == 0)
					priceCredit.bigToString() + " " + stringResource(R.string.vehicle_price_credits)
				else
					NO_DATA
			}
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_price_price),
				value = textPrice,
			)
		}
	}
	
}

@Composable
fun SingleTechnicsCard(
	item: VehicleData,
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
			text = stringResource(R.string.title_details),
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
			style = MaterialTheme.typography.headlineSmall
		)
		MainDivider()
		
		Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))) {
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_battles),
				value = item.battles.bigToString()
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_victories),
				value = item.wins.bigToString()
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_win_rate),
				value = item.winRateLabel
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_mastery),
				value = null,
				painter = painterResource(masteryToResId(item.markOfMastery))
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun SingleTechnicsCardItemPreview() {
	DynamicAssistantTheme {
		Surface(
			color = MaterialTheme.colorScheme.background
		) {
			SingleTechnicsContent(
				shortData = listOf(VehicleData(
					tankId = 1,
					markOfMastery = 1,
					battles = 256,
					wins = 130,
					winRate = 50.78f,
					winRateLabel = getMainAvg(130, 256)
						.toScreen(100f, "%"),
					lastBattleTime = 1669914970.asStringDate("short"),
					name = "T-34",
					type = "mediumTank",
					description = "description",
					nation = "ussr",
					urlSmallIcon = "urlSmallIcon",
					urlBigIcon = "urlBigIcon",
					tier = 5,
					priceGold = 0,
					priceCredit = 456456,
					isPremium = false,
					isGift = false,
					isWheeled = false,
					stat = emptyList()
				)), singleId = 1
			)
		}
	}
}