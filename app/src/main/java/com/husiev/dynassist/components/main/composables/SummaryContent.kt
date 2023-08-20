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
import com.husiev.dynassist.components.main.utils.MainRoutesData

@Composable
fun SummaryContent(
	summaryHeaders: MainRoutesData,
	summaryData: AccountStatisticsData?,
	modifier: Modifier = Modifier,
	title: String = "",
	onClick: (String) -> Unit = {}
) {
	val state = rememberLazyListState()
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		repeat(summaryHeaders.headers.size) { index ->
			item {
				MainCard(
					title = summaryHeaders.headers[index],
					items = summaryHeaders.listItems[index],
					data = summaryHeaders.listItems[index].map { it.length }
				)
			}
		}
	}
}

//@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun SummaryContentPreview() {
//	DynamicAssistantTheme {
//		SummaryContent()
//	}
//}