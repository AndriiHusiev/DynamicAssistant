package com.husiev.dynassist.components.start.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import java.util.Date

@Composable
fun SearchListItem(
	accountInfo: StartAccountInfo,
	modifier: Modifier = Modifier,
	onClick: (StartAccountInfo) -> Unit = {}
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.clickable(onClick = { onClick(accountInfo) }),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = accountInfo.nickname,
				modifier = Modifier
					.padding(
						horizontal = dimensionResource(R.dimen.padding_large),
						vertical = dimensionResource(R.dimen.padding_small)
					),
				overflow = TextOverflow.Ellipsis,
				maxLines = 1,
			)
		}
		Divider(
			modifier = Modifier.padding(
				horizontal = dimensionResource(R.dimen.padding_medium)
			),
		)
	}
}

@Preview(showBackground = true)
@Composable
fun SearchListItemPreview() {
	DynamicAssistantTheme {
		val dateTime = Date().time.toString()
		SearchListItem(
			accountInfo = StartAccountInfo(1,"nickname", updateTime = dateTime, notification = NotifyEnum.UNCHECKED)
		)
	}
}