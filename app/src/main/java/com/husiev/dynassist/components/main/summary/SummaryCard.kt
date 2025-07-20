package com.husiev.dynassist.components.main.summary

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.FullAccStatData
import com.husiev.dynassist.components.main.utils.DaElevatedCard

@Composable
fun SummaryCard(
	header: String,
	summaryData: List<FullAccStatData>,
	modifier: Modifier = Modifier,
	onClick: (Int) -> Unit = {},
) {
	DaElevatedCard(modifier = modifier) {
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
					onClick = { onClick(item.statId) }
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