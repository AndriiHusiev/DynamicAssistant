package com.husiev.dynassist.components.main.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.start.composables.NotifyEnum
import com.husiev.dynassist.database.entity.asStringDate
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun DetailsContent(
	modifier: Modifier = Modifier,
	viewModel: DetailsViewModel = hiltViewModel(),
) {
	val personalData by viewModel.personalData.collectAsStateWithLifecycle()
	val clanData by viewModel.clanData.collectAsStateWithLifecycle()
	val notifyState by viewModel.notifyState.collectAsStateWithLifecycle()
	val state = rememberLazyListState()
	
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		state = state,
		verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_big)),
		contentPadding = PaddingValues(dimensionResource(R.dimen.padding_big)),
	) {
		item { DetailsRatingCard(personalData?.globalRating) }
		item { DetailsClanCard(clanData) }
		item {
			DetailsDateCard(
				detailsData = personalData,
				notifyState = notifyState,
				onNotifyClick = viewModel::switchNotification
			)
		}
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
			Column {
				DetailsRatingCard(globalRating = 10563)
				DetailsClanCard(null)
				DetailsDateCard(
					detailsData = AccountPersonalData(
						accountId = 1,
						nickname = "CoolNickname",
						lastBattleTime = 1669914970.asStringDate(),
						createdAt = 1282720111.asStringDate(),
						updatedAt = 1692550413.asStringDate(),
						logoutAt = 1687009120.asStringDate(),
						clanId = null,
						globalRating = 10563
					),
					notifyState = NotifyEnum.UNCHECKED,
				)
			}
//				AccountClanInfo(
//					accountId = 1,
//					joinedAt = 1691693119,
//					roleLocalized = "Private",
//					clanId = 502345049,
//					createdAt = 1422654206,
//					membersCount = 42,
//					name = "We are newbies",
//					tag = "NOOB",
//					color = "#6D12A0",
//					emblem = "https://eu.wargaming.net/clans/media/clans/emblems/" +
//							"cl_042/502345049/emblem_195x195.png",
//				)
		}
	}
}