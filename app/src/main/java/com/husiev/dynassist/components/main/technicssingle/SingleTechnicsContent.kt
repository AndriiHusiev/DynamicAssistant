package com.husiev.dynassist.components.main.technicssingle

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.husiev.dynassist.components.main.utils.*
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SingleTechnicsContent(
	modifier: Modifier = Modifier,
	viewModel: SingleTechnicsViewModel = hiltViewModel(),
) {
	val state = rememberLazyListState()
	val vehicleData by viewModel.vehicleData.collectAsStateWithLifecycle()
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
	) {
		vehicleData?.let { item ->
			item {
				SingleTechnicsImageCard(
					urlIcon = item.info.urlBigIcon,
					nation = item.info.nation,
					description = item.info.description,
					role = item.info.type,
					tier = item.info.tier,
					priceCredit = item.info.priceCredit,
					priceGold = item.info.priceGold,
					modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
				)
			}
			item {
				SmoothLineGraph(
					graphData = item.victories,
					dateData = item.dates,
					modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_big))
				)
			}
			
			item {
				SingleTechnicsCard(
					item = item.ui,
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
	item: VehicleUiData,
	modifier: Modifier = Modifier,
) {
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
				value = item.battles
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_victories),
				value = item.victories
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.vehicle_win_rate),
				value = item.winRate
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.average_value_per_session),
				value = item.sessionAvgValue
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.session_impact),
				value = item.sessionImpactValue,
				imageVector = item.imageVector,
				color = item.color
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
				
/*				SingleTechnicsCard(
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
								statId = 0,
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
								statId = 0,
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
				)*/
			}
		}
	}
}