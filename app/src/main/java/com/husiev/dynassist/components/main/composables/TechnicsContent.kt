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
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.SHORT_PATTERN
import com.husiev.dynassist.components.main.utils.VehicleData
import com.husiev.dynassist.database.entity.asStringDate
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TechnicsContent(
	shortData: List<VehicleData>,
	sort: SortTechnics,
	filter: FilterTechnics,
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
		val lastBattleTime = getLastDate(shortData)
		
		shortData
			.sortedWith(getComparator(sort))
			.filter { getFilter(filter, it, lastBattleTime) }
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
	SortTechnics.TYPE -> compareByDescending(VehicleData::type)
		.thenByDescending(VehicleData::nation)
		.thenByDescending(VehicleData::tier)
		.thenByDescending(VehicleData::battles)
		.thenByDescending(VehicleData::winRate)
		.thenByDescending(VehicleData::isPremium)
	SortTechnics.LEVEL -> compareByDescending(VehicleData::tier)
		.thenByDescending(VehicleData::nation)
		.thenByDescending(VehicleData::type)
		.thenByDescending(VehicleData::battles)
		.thenByDescending(VehicleData::winRate)
		.thenByDescending(VehicleData::isPremium)
	SortTechnics.NATION -> compareByDescending(VehicleData::nation)
		.thenByDescending(VehicleData::type)
		.thenByDescending(VehicleData::tier)
		.thenByDescending(VehicleData::battles)
		.thenByDescending(VehicleData::winRate)
		.thenByDescending(VehicleData::isPremium)
	SortTechnics.WINRATING -> compareByDescending(VehicleData::winRate)
		.thenByDescending(VehicleData::battles)
		.thenByDescending(VehicleData::type)
		.thenByDescending(VehicleData::tier)
		.thenByDescending(VehicleData::nation)
		.thenByDescending(VehicleData::isPremium)
	SortTechnics.PREMIUM -> compareByDescending(VehicleData::isPremium)
		.thenByDescending(VehicleData::nation)
		.thenByDescending(VehicleData::type)
		.thenByDescending(VehicleData::tier)
		.thenByDescending(VehicleData::battles)
		.thenByDescending(VehicleData::winRate)
	else -> compareByDescending(VehicleData::battles)
		.thenByDescending(VehicleData::tier)
		.thenByDescending(VehicleData::type)
		.thenByDescending(VehicleData::nation)
		.thenByDescending(VehicleData::winRate)
		.thenByDescending(VehicleData::isPremium)
}

fun getLastDate(shortData: List<VehicleData>): String {
	val lastDate = shortData.maxOfOrNull {
		if (it.lastBattleTime == NO_DATA)
			0
		else
			SimpleDateFormat(SHORT_PATTERN, Locale.getDefault())
				.parse(it.lastBattleTime)
				?.time ?: 0
			} ?: 0
	
	return (lastDate / 1000)
		.toInt()
		.asStringDate("short")
}

fun getFilter(
	filter: FilterTechnics,
	vehicle: VehicleData,
	lastBattleTime: String
) = when(filter) {
	FilterTechnics.LIGHT,
	FilterTechnics.MEDIUM,
	FilterTechnics.HEAVY,
	FilterTechnics.ATSPG,
	FilterTechnics.SPG -> vehicle.type == filter.alias
	
	FilterTechnics.LAST -> vehicle.lastBattleTime == lastBattleTime
	
	else -> true
}