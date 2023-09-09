package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.VehicleShortData
import com.husiev.dynassist.components.main.utils.flagToResId
import com.husiev.dynassist.components.main.utils.masteryToResId
import com.husiev.dynassist.components.main.utils.roleToResId
import com.husiev.dynassist.components.main.utils.symbolToResId
import com.husiev.dynassist.components.main.utils.tierToResId
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun TechnicsCard(
	shortData: VehicleShortData,
	modifier: Modifier = Modifier,
	onClick: (VehicleShortData) -> Unit = {}
) {
	ElevatedCard(
		modifier = modifier
			.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
			.fillMaxWidth()
			.clickable { onClick(shortData) },
		shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
		Box(modifier = Modifier) {
				Column(modifier = Modifier.fillMaxWidth()) {
					Row(
						modifier = Modifier
							.background(Color(0x66000000))
							.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Row(verticalAlignment = Alignment.CenterVertically) {
							Image(
								painter = painterResource(symbolToResId(shortData.nation)),
//								painter = painterResource(symbolToResId("ussr")),
								contentDescription = null,
								modifier = Modifier
									.size(32.dp, 32.dp)
									.padding(
										horizontal = dimensionResource(R.dimen.padding_small),
										vertical = dimensionResource(R.dimen.padding_extra_small)
									)
							)
							Image(
								painter = painterResource(roleToResId(shortData.type)),
								contentDescription = null,
								modifier = Modifier.size(15.dp, 30.dp),
							)
							Image(
								painter = painterResource(tierToResId(shortData.tier)),
								contentDescription = null,
								modifier = Modifier
									.padding(horizontal = dimensionResource(R.dimen.padding_small))
									.size(22.dp, 22.dp),
							)
						}
						Text(
							text = "last_battle_date",
							modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_extra_large)),
							style = MaterialTheme.typography.bodySmall
						)
					}
					
					
					Row(
						modifier = Modifier
							.align(Alignment.End)
							.padding(
								end = dimensionResource(R.dimen.padding_extra_small),
								top = dimensionResource(R.dimen.padding_medium)
							),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Column(
							modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_extra_small)),
							horizontalAlignment = Alignment.End
						) {
							Text(
								text = (100f * shortData.wins / shortData.battles).toString() + "%",
								style = MaterialTheme.typography.bodyLarge
							)
							
							Row(verticalAlignment = Alignment.CenterVertically) {
	//							if (auxValue != null && imageVector != null && color != null) {
								Icon(
									imageVector = Icons.Filled.ArrowDropUp,
									contentDescription = null,
									tint = Color.Green
								)
	//							}
								
								Text(
									text = shortData.battles.toString(),//"auxValue",
									style = MaterialTheme.typography.bodySmall
								)
								
							}
						}
						
						Icon(
							imageVector = Icons.Filled.ChevronRight,
							contentDescription = null
						)
					}
				}
			
			Image(
				painter = painterResource(flagToResId(shortData.nation)),
				contentDescription = null,
				modifier = Modifier.size(251.dp, 92.dp)
			)
			
			Image(
				painter = painterResource(R.drawable.ic_tank_empty),
				contentDescription = null,
				modifier = Modifier
					.padding(horizontal = dimensionResource(R.dimen.padding_extra_large))
					.size(145.dp, 92.dp)
			)
		}
		
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Image(
				painter = painterResource(shortData.markOfMastery),
				contentDescription = null,
				modifier = Modifier
					.padding(horizontal = dimensionResource(R.dimen.padding_small))
					.size(22.dp, 22.dp)
			)
			
			Text(
				text = shortData.name ?: NO_DATA,
				modifier = Modifier.padding(
					vertical = dimensionResource(R.dimen.padding_extra_small)
				),
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun TechnicsContentPreview() {
	DynamicAssistantTheme {
		TechnicsCard(
			shortData = VehicleShortData(
				tankId = 1,
				markOfMastery = 4.masteryToResId(),
				battles = 996,
				wins = 598,
				name = "T-34",
				type = "mediumTank",
				description = "desc",
				nation = "ussr",
				urlSmallIcon = "",
				urlBigIcon = "",
				tier = 5,
				priceGold = 0,
				priceCredit = 356700,
				isPremium = false,
				isGift = false,
				isWheeled = false,
			),
		)
	}
}