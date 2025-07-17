package com.husiev.dynassist.components.main.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.R

@Composable
fun SummaryContent(
	modifier: Modifier = Modifier,
	onClick: (String) -> Unit = {},
	viewModel: SummaryViewModel = hiltViewModel(),
) {
	val statisticData by viewModel.statisticData.collectAsStateWithLifecycle()
	val state = rememberLazyListState()
//	val statisticData = mapOf("Overall results" to statistic1Data, "Battle Performance" to statistic1Data)
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		statisticData.forEach { (group, item) ->
//			if (item.key == "Other") return@forEach
			item {
				SummaryCard(
					header = stringResource(id = group.titleResId),
					summaryData = item,
					onClick = onClick
				)
			}
		}
	}
}