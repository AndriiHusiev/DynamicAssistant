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
import com.husiev.dynassist.components.main.utils.AccountStatisticsData

@Composable
fun SummaryContent(
	summaryData: Map<String, List<AccountStatisticsData>>,
	modifier: Modifier = Modifier,
	onClick: (String) -> Unit = {}
) {
	val state = rememberLazyListState()
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		summaryData.forEach { item ->
			item {
				MainSummaryCard(
					header = item.key,
					summaryData = item.value,
					onClick = onClick
				)
			}
		}
	}
}