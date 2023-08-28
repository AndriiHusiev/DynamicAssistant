package com.husiev.dynassist.components.main.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.database.entity.asStringDate
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun DetailsContent(
	detailsData: AccountPersonalData?,
	modifier: Modifier = Modifier,
) {
	val state = rememberLazyListState()
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		item {
			ElevatedCard(
				modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium))),
				shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
				elevation = CardDefaults.elevatedCardElevation(
					dimensionResource(R.dimen.padding_extra_small)
				)
			) {
				Text(
					text = detailsData?.nickname ?: NO_DATA,
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
				
				MainDivider()
				DetailsCardItem(
					title = stringResource(R.string.logout_at),
					text = detailsData?.logoutAt ?: NO_DATA
				)
				
				MainDivider()
				DetailsCardItem(
					title = stringResource(R.string.updated_at),
					text = detailsData?.updatedAt ?: NO_DATA
				)
				
				MainDivider()
				DetailsCardItem(
					title = stringResource(R.string.created_at),
					text = detailsData?.createdAt ?: NO_DATA
				)
			}
		}
	}
}

@Composable
fun DetailsCardItem(
	title: String,
	text: String,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = title,
			modifier = Modifier
				.padding(
					horizontal = dimensionResource(R.dimen.padding_medium),
					vertical = dimensionResource(R.dimen.padding_large),
				),
			style = MaterialTheme.typography.bodyLarge
		)
		
		Text(
			text = text,
			modifier = Modifier
				.padding(
					horizontal = dimensionResource(R.dimen.padding_medium),
					vertical = dimensionResource(R.dimen.padding_large),
				),
			style = MaterialTheme.typography.bodyMedium
		)
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailsContentPreview() {
	DynamicAssistantTheme {
		Surface(
			color = MaterialTheme.colorScheme.background
		) {
//			DetailsContent(null)
			
			DetailsContent(
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