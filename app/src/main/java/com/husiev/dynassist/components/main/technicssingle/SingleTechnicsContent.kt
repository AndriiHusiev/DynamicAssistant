package com.husiev.dynassist.components.main.technicssingle

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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
import com.husiev.dynassist.ui.theme.md_theme_dark_secondary

private const val FlagRatio = 278f / 102f
private val titleHeight = 52.dp
private val minHeaderHeight = titleHeight * 2f
private var maxHeaderHeight = 240.dp
private var expandedImageSize = maxHeaderHeight - titleHeight
private val collapsedImageSize = minHeaderHeight

@Composable
fun SingleTechnicsContent(
	upPress: () -> Unit,
	modifier: Modifier = Modifier,
	viewModel: SingleTechnicsViewModel = hiltViewModel(),
) {
	val vehicleData by viewModel.vehicleData.collectAsStateWithLifecycle()
	val screenWidth = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
	maxHeaderHeight = screenWidth / 1.6f
	
	Box(
		modifier = modifier.fillMaxSize(),
	) {
		val scroll = rememberScrollState(0)
		Body(vehicleData, scroll)
		Header(vehicleData?.info?.nation ?: "") { scroll.value }
		Title(vehicleData?.info?.name ?: "--") { scroll.value }
		Image(vehicleData?.info?.urlBigIcon) { scroll.value }
		Up(upPress)
	}
}

@Composable
private fun Header(flag: String, scrollProvider: () -> Int) {
	val density = LocalDensity.current
	val maxHeight = with(density) { (maxHeaderHeight).toPx() }
	val minHeight = with(density) { (minHeaderHeight).toPx() }
	val scroll = scrollProvider()
	val height = with(density) { (maxHeight - scroll).coerceAtLeast(minHeight).toDp() }
	
	Column(modifier = Modifier.fillMaxWidth().background(md_theme_dark_secondary)) {
		Box(
			modifier = Modifier
				.size(height * FlagRatio, height)
		) {
			Image(
				painter = painterResource(flagToResId(flag)),
				contentDescription = null,
				modifier = Modifier.fillMaxHeight(),
				alignment = Alignment.CenterStart,
				contentScale = ContentScale.FillHeight
			)
		}
		HorizontalDivider(color = Color.Black)
	}
}

@Composable
private fun Title(title: String, scrollProvider: () -> Int) {
	val density = LocalDensity.current
	val maxOffset = with(density) { (maxHeaderHeight - titleHeight).toPx() }
	val minOffset = with(density) { ((minHeaderHeight - titleHeight)).toPx() }
	
	val gradientBrush = Brush.verticalGradient(listOf(
		Color(0x66000000),
		Color(0x00000000)))
	
	Column(
		verticalArrangement = Arrangement.Bottom,
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(min = titleHeight)
			.offset {
				val scroll = scrollProvider()
				val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
				IntOffset(x = 0, y = offset.toInt())
			}
			.background(gradientBrush)
	) {
		Text(
			text = title,
			modifier = Modifier.padding(horizontal = 16.dp)
				.fillMaxWidth()
				.padding(8.dp),
			overflow = TextOverflow.Ellipsis,
			maxLines = 1,
			style = MaterialTheme.typography.headlineSmall,
			fontWeight = FontWeight.Bold
		)
	}
}

@Composable
private fun Body(vehicleData: VehicleData?, scroll: ScrollState) {
	Column(
		modifier = Modifier.verticalScroll(scroll),
	) {
		Spacer(Modifier.height(maxHeaderHeight))
		
		Box(Modifier.fillMaxWidth()){
			Column {
				vehicleData?.let { item ->
					SingleTechnicsImageCard(
						description = item.info.description,
						role = item.info.type,
						tier = item.info.tier,
						priceCredit = item.info.priceCredit,
						priceGold = item.info.priceGold,
						modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
					)
					
					SmoothLineGraph(
						graphData = item.victories,
						dateData = item.dates,
						modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_big))
					)
					
					SingleTechnicsCard(
						item = item.ui,
						modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
					)
				}
			}
		}
	}
}

@Composable
private fun Image(
	urlIcon: String?,
	scrollProvider: () -> Int,
) {
	val density = LocalDensity.current
	val gradientBrush = Brush.radialGradient(
		colors = listOf(Color(0xFF515138), Color(0x00515138)),
		radius = with(density) { minHeaderHeight.toPx() / 2f }
	)
	expandedImageSize = maxHeaderHeight - titleHeight
	val scroll = scrollProvider()
	val maxHeight = with(density) { (expandedImageSize).toPx() }
	val minHeight = with(density) { (collapsedImageSize).toPx() }
	val height = with(density) { (maxHeight - scroll).coerceAtLeast(minHeight).toDp() }
	val width = height * 1.6f
	
	Box(
		modifier = Modifier.fillMaxWidth(),
		contentAlignment = Alignment.CenterEnd
	) {
		AsyncImage(
			model = urlIcon,
			error = painterResource(R.drawable.ic_tank_empty),
			placeholder = painterResource(R.drawable.ic_tank_empty),
			contentDescription = null,
			modifier = Modifier
				.background(gradientBrush)
				.size(width, height)
		)
	}
}

@Composable
private fun Up(upPress: () -> Unit) {
	IconButton(
		onClick = upPress,
		modifier = Modifier
			.statusBarsPadding()
			.padding(horizontal = 8.dp, vertical = 8.dp)
			.size(36.dp)
			.background(
				color = Color(0x52121212),
				shape = CircleShape,
			),
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
			tint = Color.White,
			contentDescription = stringResource(R.string.description_back),
		)
	}
}

@Composable
fun SingleTechnicsImageCard(
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
//					urlIcon = "https://api.worldoftanks.eu/static/2.71.0/wot/encyclopedia/vehicle/ussr-R04_T-34.png",
//					nation = "ussr",
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