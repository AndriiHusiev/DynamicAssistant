package com.husiev.dynassist.components.main.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.asInitial
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SummaryContent(
	summaryData: List<AccountStatisticsData>,
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
		repeat(summaryData[0].headers.size) { index ->
			item {
				MainCard(
					title = summaryData[0].headers[index],
					items = summaryData[0].items[index],
					divider = summaryData[0].divider,
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SummaryContentPreview() {
	DynamicAssistantTheme {
		SummaryContent(asInitial(listOf("Header 1","Header 2","Header 3","Header 4")))
	}
}