package com.husiev.dynassist.components.main.technicssingle

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.composables.SmoothLineGraph
import com.husiev.dynassist.components.main.summary.MainDivider
import com.husiev.dynassist.components.main.summarysingle.SingleSummaryCardItem
import com.husiev.dynassist.components.main.technics.TechnicsViewModel
import com.husiev.dynassist.components.main.utils.ReducedAccStatData
import com.husiev.dynassist.components.main.utils.DaElevatedCard
import com.husiev.dynassist.components.main.utils.FullAccStatData
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.SummaryGroup
import com.husiev.dynassist.components.main.utils.VehicleData
import com.husiev.dynassist.components.main.utils.bigToString
import com.husiev.dynassist.components.main.utils.flagToResId
import com.husiev.dynassist.components.main.utils.masteryToResId
import com.husiev.dynassist.components.main.utils.roleResId
import com.husiev.dynassist.database.entity.asStringDate
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SingleTechnicsContent(
	modifier: Modifier = Modifier,
	singleId: Int? = null,
	viewModel: TechnicsViewModel = hiltViewModel(),
) {
	val state = rememberLazyListState()
	val vehicleData by viewModel.vehicleData.collectAsStateWithLifecycle()
	val singleItem = vehicleData.singleOrNull { it.tankId == singleId }
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
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
					modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
				)
			}
			item {
//				SmoothLineGraph(
//					item.stat[1].values,
//					item.stat[2].values?.map { it.toInt().asStringDate("shortest") },
//					Modifier.padding(horizontal = dimensionResource(R.dimen.padding_big))
//				)
			}
			
			item {
				SingleTechnicsCard(
					item = item,
					modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
				)
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
	
	Box(
		modifier = Modifier
			.animateContentSize(
				animationSpec = spring(
					dampingRatio = Spring.DampingRatioMediumBouncy,
					stiffness = Spring.StiffnessLow
				)
			)
	) {
		DaElevatedCard(
			onClick = { expanded = !expanded },
			modifier = modifier,
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
	
}

@Composable
fun SingleTechnicsCard(
	item: VehicleData,
	modifier: Modifier = Modifier,
) {
	val battles = item.stat[0]
	val wins = item.stat[1]
	
	DaElevatedCard(modifier = modifier) {
		Text(
			text = stringResource(R.string.title_details),
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
			style = MaterialTheme.typography.headlineSmall
		)
		MainDivider()
		
		Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))) {
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_battles),
				value = item.battles.doubleBubble(battles.sessionAbsValue)
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_victories),
				value = item.wins.doubleBubble(wins.sessionAbsValue)
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_win_rate),
				value = wins.mainValue
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.average_value_per_session),
				value = wins.sessionAvgValue
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.session_impact),
				value = wins.sessionImpactValue,
				imageVector = wins.imageVector,
				color = wins.color
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_mastery),
				value = null,
				painter = painterResource(masteryToResId(item.markOfMastery))
			)
		}
	}
}

fun Int.doubleBubble(session: String?): String {
	return if (session != null && session != NO_DATA)
		"$session / " + this.bigToString()
	else
		this.bigToString()
}

@Preview(showBackground = true)
@Composable
fun SingleTechnicsCardItemPreview() {
	DynamicAssistantTheme {
		Surface(
			color = MaterialTheme.colorScheme.background
		) {
			Column {
				SingleTechnicsImageCard(
					urlIcon = "https://api.worldoftanks.eu/static/2.71.0/wot/encyclopedia/vehicle/ussr-R04_T-34.png",
					nation = "ussr",
					description = "The legend of the Soviet armored forces and the most widely-produced Soviet tank of World War II, with a total of 33,805 vehicles manufactured. Three variants of this model were produced at several Soviet factories from 1940 through 1944.",
					role = "mediumTank",
					tier = 5,
					priceCredit = 456456,
					priceGold = 0,
					modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
				)
				
				SmoothLineGraph(null, null,
					Modifier.padding(horizontal = dimensionResource(R.dimen.padding_big)))
				
				SingleTechnicsCard(
					item = VehicleData(
						tankId = 1,
						markOfMastery = 1,
						battles = 256,
						wins = 130,
						winRate = 50.78f,
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
						stat = listOf(
							FullAccStatData(
								title = "Battles",
								mainValue = "256",
								auxValue = null,
								absValue = "256",
								sessionAbsValue = "+6",
								sessionAvgValue = null,
								sessionImpactValue = null,
								color = null,
								imageVector = null,
								comment = null,
								group = SummaryGroup.OVERALL_RESULTS
							),
							FullAccStatData(
								title = "Wins",
								mainValue = "50.8%",
								auxValue = "0.0078 / 83.3%",
								absValue = "130",
								sessionAbsValue = "+5",
								sessionAvgValue = "83.3%",
								sessionImpactValue = "+0.0078",
								color = Color(0xFF009688),
								imageVector = Icons.Filled.ArrowDropUp,
								comment = null,
								group = SummaryGroup.OVERALL_RESULTS
							),
						)
					),
					modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
				)
			}
		}
	}
}