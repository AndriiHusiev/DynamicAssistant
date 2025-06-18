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
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		statisticData.forEach { item ->
			if (item.key == "Other") return@forEach
			item {
				SummaryCard(
					header = item.key,
					summaryData = item.value,
					onClick = onClick
				)
			}
		}
	}
}