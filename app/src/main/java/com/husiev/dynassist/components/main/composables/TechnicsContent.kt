package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.VehicleShortData

@Composable
fun TechnicsContent(
	shortData: List<VehicleShortData>,
	sort: SortTechnics,
	modifier: Modifier = Modifier,
	onClick: (Int) -> Unit = {}
) {
	val state = rememberLazyListState()
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		shortData
			.sortedWith(getComparator(sort))
			.forEach { item ->
				item {
					TechnicsCard(
						shortData = item,
						onClick = onClick
					)
				}
		}
	}
}

fun getComparator(sort: SortTechnics) = when(sort) {
	SortTechnics.TYPE -> compareByDescending(VehicleShortData::type)
		.thenByDescending(VehicleShortData::nation)
		.thenByDescending(VehicleShortData::tier)
		.thenByDescending(VehicleShortData::battles)
		.thenByDescending(VehicleShortData::winRate)
		.thenByDescending(VehicleShortData::isPremium)
	SortTechnics.LEVEL -> compareByDescending(VehicleShortData::tier)
		.thenByDescending(VehicleShortData::nation)
		.thenByDescending(VehicleShortData::type)
		.thenByDescending(VehicleShortData::battles)
		.thenByDescending(VehicleShortData::winRate)
		.thenByDescending(VehicleShortData::isPremium)
	SortTechnics.NATION -> compareByDescending(VehicleShortData::nation)
		.thenByDescending(VehicleShortData::type)
		.thenByDescending(VehicleShortData::tier)
		.thenByDescending(VehicleShortData::battles)
		.thenByDescending(VehicleShortData::winRate)
		.thenByDescending(VehicleShortData::isPremium)
	SortTechnics.WINRATING -> compareByDescending(VehicleShortData::winRate)
		.thenByDescending(VehicleShortData::battles)
		.thenByDescending(VehicleShortData::type)
		.thenByDescending(VehicleShortData::tier)
		.thenByDescending(VehicleShortData::nation)
		.thenByDescending(VehicleShortData::isPremium)
	SortTechnics.PREMIUM -> compareByDescending(VehicleShortData::isPremium)
		.thenByDescending(VehicleShortData::nation)
		.thenByDescending(VehicleShortData::type)
		.thenByDescending(VehicleShortData::tier)
		.thenByDescending(VehicleShortData::battles)
		.thenByDescending(VehicleShortData::winRate)
	else -> compareByDescending(VehicleShortData::battles)
		.thenByDescending(VehicleShortData::tier)
		.thenByDescending(VehicleShortData::type)
		.thenByDescending(VehicleShortData::nation)
		.thenByDescending(VehicleShortData::winRate)
		.thenByDescending(VehicleShortData::isPremium)
}