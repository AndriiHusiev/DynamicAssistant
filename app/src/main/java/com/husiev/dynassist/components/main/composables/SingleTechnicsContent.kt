package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.Image
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.VehicleShortData
import com.husiev.dynassist.components.main.utils.flagToResId
import com.husiev.dynassist.components.main.utils.getMainAvg
import com.husiev.dynassist.components.main.utils.toScreen
import com.husiev.dynassist.database.entity.asStringDate
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SingleTechnicsContent(
	shortData: List<VehicleShortData>,
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
	modifier: Modifier = Modifier,
) {
	ElevatedCard(
		modifier = modifier
			.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium))),
		shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
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
		
	}
	
}

@Composable
fun SingleTechnicsCard(
	item: VehicleShortData,
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
			SingleTechnicsCardItem(
				title = stringResource(R.string.total_amount),
				value = item.name
			)
			
			SingleTechnicsCardItem(
				title = stringResource(R.string.average_value),
				value = item.name
			)
		}
	}
}

@Composable
fun SingleTechnicsCardItem(
	title: String,
	value: String?,
	modifier: Modifier = Modifier,
	imageVector: ImageVector? = null,
	color: Color? = null
) {
	Row(
		modifier = modifier
			.padding(
				horizontal = dimensionResource(R.dimen.padding_big),
				vertical = dimensionResource(R.dimen.padding_small)
			)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.bodyLarge
		)
		
		Row(verticalAlignment = Alignment.CenterVertically) {
			if (value != null && imageVector != null && color != null) {
				Icon(
					imageVector = imageVector,
					contentDescription = null,
					tint = color
				)
			}
			
			Text(
				text = value ?: NO_DATA,
				style = MaterialTheme.typography.bodyMedium
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
				shortData = listOf(VehicleShortData(
					tankId = 1,
					markOfMastery = 1,
					battles = 256,
					wins = 130,
					winRate = getMainAvg(130, 256).toScreen(100f, "%"),
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
					isWheeled = false
				)), singleId = 1
			)
		}
	}
}