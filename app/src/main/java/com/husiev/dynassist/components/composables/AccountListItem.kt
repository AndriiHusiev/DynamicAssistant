package com.husiev.dynassist.components.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.husiev.dynassist.R
import com.husiev.dynassist.components.utils.StartAccountInfo

@Composable
fun AccountListItem(
	account: StartAccountInfo,
	modifier: Modifier = Modifier,
	clanEmblem: Painter = painterResource(id = R.drawable.no_clan),
	onClick: () -> Unit = {}
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.background(color = MaterialTheme.colorScheme.secondaryContainer)
			.clickable(onClick = onClick),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Image(
				painter = clanEmblem,
				contentDescription = null,
				modifier = Modifier.padding(
					horizontal = dimensionResource(R.dimen.padding_small),
					vertical = dimensionResource(R.dimen.padding_medium)
				)
			)
			Text(
				text = account.nickname,
				modifier = Modifier
					.weight(1f,false)
					.padding(start = dimensionResource(R.dimen.padding_small)),
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				overflow = TextOverflow.Ellipsis,
				maxLines = 1,
			)
			if (account.clan != null) {
				Text(
					text = "[${account.clan}]",
					modifier = Modifier
						.padding(
							start = 2.dp,
							end = dimensionResource(R.dimen.padding_small),
						),
					color = MaterialTheme.colorScheme.onSecondaryContainer,
				)
			}
		}
		Divider(
			modifier = Modifier.padding(
				horizontal = dimensionResource(R.dimen.padding_medium)
			),
			color = MaterialTheme.colorScheme.outline
		)
	}
}