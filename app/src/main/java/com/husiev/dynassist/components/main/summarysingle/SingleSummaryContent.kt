package com.husiev.dynassist.components.main.summarysingle

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.composables.SmoothLineGraph
import com.husiev.dynassist.components.main.summary.MainDivider
import com.husiev.dynassist.components.main.summary.SummaryViewModel
import com.husiev.dynassist.components.main.utils.ReducedAccStatData
import com.husiev.dynassist.components.main.utils.DaElevatedCard
import com.husiev.dynassist.components.main.utils.FullAccStatData
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.SummaryGroup
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun SingleSummaryContent(
	modifier: Modifier = Modifier,
	singleTitle: String? = null,
	viewModel: SingleSummaryViewModel = hiltViewModel(),
) {
	val statisticData by viewModel.statisticData.collectAsStateWithLifecycle()
	val state = rememberLazyListState()
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		statisticData?.let { item ->
			item { SmoothLineGraph(item.values, item.dates) }
			
			item {
				SingleSummaryCard(item = item.item)
			}
		}
	}
}

@Composable
fun SingleSummaryCard(
	item: FullAccStatData,
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
				title = stringResource(R.string.total_amount),
				value = item.absValue
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.average_value),
				value = item.mainValue
			)
			
			SingleSummaryCardItem(
				title = stringResource(R.string.total_amount_per_session),
				value = item.sessionAbsValue
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
		}
	}
}

@Composable
fun SingleSummaryCardItem(
	title: String,
	value: String?,
	modifier: Modifier = Modifier,
	imageVector: ImageVector? = null,
	painter: Painter? = null,
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
			
			if (painter == null) {
				Text(
					text = value ?: NO_DATA,
					style = MaterialTheme.typography.bodyMedium
				)
			} else {
				Image(
					painter = painter,
					contentDescription = null,
					modifier = Modifier.size(22.dp, 22.dp)
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SingleSummaryCardItemPreview() {
	DynamicAssistantTheme {
		Surface(
			color = MaterialTheme.colorScheme.background
		) {
			Column {
				SingleSummaryCard(
					item = FullAccStatData(
						title = "Victories",
						mainValue = "56.3%",
						absValue = "246",
						auxValue = "+0.024% / 58.2%",
						sessionAbsValue = "+5",
						sessionAvgValue = "58.2%",
						sessionImpactValue = "+0.024%",
						color = Color.Green,
						imageVector = Icons.Filled.ArrowDropUp,
						group = SummaryGroup.OVERALL_RESULTS
					)
				)
			}
		}
	}
}