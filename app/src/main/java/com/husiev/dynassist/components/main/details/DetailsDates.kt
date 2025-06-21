package com.husiev.dynassist.components.main.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.summary.MainDivider
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.DaElevatedCard
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.start.composables.NotifyEnum
import com.husiev.dynassist.database.entity.asStringDate
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun DetailsDateCard(
	detailsData: AccountPersonalData?,
	modifier: Modifier = Modifier,
	notifyState: NotifyEnum = NotifyEnum.UNCHECKED,
	onNotifyClick: (Boolean) -> Unit = {},
) {
	DaElevatedCard(modifier = modifier) {
		Text(
			text = stringResource(R.string.events_time),
			modifier = Modifier
				.padding(dimensionResource(R.dimen.padding_big))
				.fillMaxWidth(),
			textAlign = TextAlign.Center,
			overflow = TextOverflow.Ellipsis,
			maxLines = 1,
			style = MaterialTheme.typography.headlineSmall
		)
		
		MainDivider()
		
		DetailsCardItem(
			title = stringResource(R.string.last_battle_time),
			text = detailsData?.lastBattleTime ?: NO_DATA
		)
		
		DetailsCardItem(
			title = stringResource(R.string.logout_at),
			text = detailsData?.logoutAt ?: NO_DATA
		)
		
		DetailsCardItem(
			title = stringResource(R.string.updated_at),
			text = detailsData?.updatedAt ?: NO_DATA
		)
		
		DetailsCardItem(
			title = stringResource(R.string.created_at),
			text = detailsData?.createdAt ?: NO_DATA
		)
		
		MainDivider()
		
		SwitchableItem(
			text = stringResource(R.string.notification_notify_text),
			checked = notifyState != NotifyEnum.UNCHECKED,
			onSwitch = onNotifyClick
		)
	}
}

@Composable
fun DetailsCardItem(
	title: String,
	text: String,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.padding(vertical = dimensionResource(R.dimen.padding_small))
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = title,
			modifier = Modifier
				.padding(
					horizontal = dimensionResource(R.dimen.padding_medium),
				),
			style = MaterialTheme.typography.bodyMedium
		)
		
		Text(
			text = text,
			modifier = Modifier
				.padding(
					horizontal = dimensionResource(R.dimen.padding_medium),
				),
			style = MaterialTheme.typography.bodySmall
		)
	}
}

@Composable
fun SwitchableItem(
	text: String,
	checked: Boolean,
	modifier: Modifier = Modifier,
	onSwitch: (Boolean) -> Unit = {}
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = text,
			modifier = Modifier.weight(1f),
			style = MaterialTheme.typography.bodyMedium
		)
		Switch(
			checked = checked,
			onCheckedChange = { onSwitch(it) }
		)
	}
}

@Preview(showBackground = true)
@Composable
fun DetailsDateCardPreview() {
	DynamicAssistantTheme {
		Column(Modifier.padding(dimensionResource(R.dimen.padding_big))) {
			DetailsDateCard(
				AccountPersonalData(
					accountId = 1,
					nickname = "CoolNickname",
					lastBattleTime = 1669914970.asStringDate(),
					createdAt = 1282720111.asStringDate(),
					updatedAt = 1692550413.asStringDate(),
					logoutAt = 1687009120.asStringDate(),
					clanId = null,
					globalRating = 10563
				)
			)
		}
	}
}