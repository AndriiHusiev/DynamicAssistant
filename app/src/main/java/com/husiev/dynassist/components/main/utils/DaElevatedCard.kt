package com.husiev.dynassist.components.main.utils

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import com.husiev.dynassist.R

@Composable
fun DaElevatedCard(
	modifier: Modifier = Modifier,
	shape: Shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
	colors: CardColors = CardDefaults.elevatedCardColors(),
	elevation: CardElevation = CardDefaults.elevatedCardElevation(
		dimensionResource(R.dimen.padding_extra_small)),
	content: @Composable ColumnScope.() -> Unit
) {
	ElevatedCard(
		modifier = modifier,
		shape = shape,
		colors = colors,
		elevation = elevation,
		content = content
	)
}

@Composable
fun DaElevatedCard(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
	colors: CardColors = CardDefaults.elevatedCardColors(),
	elevation: CardElevation = CardDefaults.elevatedCardElevation(
		dimensionResource(R.dimen.padding_extra_small)),
	content: @Composable ColumnScope.() -> Unit
) {
	ElevatedCard(
		onClick = onClick,
		modifier = modifier,
		shape = shape,
		colors = colors,
		elevation = elevation,
		content = content
	)
}