package com.husiev.dynassist.components.main.summary

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.AccountStatisticsData

@Composable
fun SummaryCard(
	header: String,
	summaryData: List<AccountStatisticsData>,
	modifier: Modifier = Modifier,
	onClick: (String) -> Unit = {},
) {
	ElevatedCard(
		modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium))),
		shape = androidx.compose.foundation.shape.RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
		Text(
			text = header,
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
			style = MaterialTheme.typography.headlineSmall
		)
		
		summaryData.forEach { item ->
			MainDivider()
			if (item.comment == null) {
				SummaryCardItem(
					title = item.title,
					mainValue = item.mainValue,
					auxValue = item.auxValue,
					absSessionValue = item.sessionAbsValue,
					color = item.color,
					imageVector = item.imageVector,
					onClick = onClick
				)
			} else {
				MainCardVehicle(
					title = item.title,
					value = item.mainValue,
					vehicleName = item.comment,
				)
			}
		}
	}
}