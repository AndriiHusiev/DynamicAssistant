package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.AccountClanInfo
import com.husiev.dynassist.database.entity.asStringDate
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme

@Composable
fun DetailsClanCard(
	clanData: AccountClanInfo?,
	modifier: Modifier = Modifier,
) {
	var sorryText = ""
	
	ElevatedCard(
		modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium))),
		shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
		Text(
			text = stringResource(R.string.clan_info),
			modifier = Modifier
				.padding(dimensionResource(R.dimen.padding_big))
				.fillMaxWidth(),
			textAlign = TextAlign.Center,
			overflow = TextOverflow.Ellipsis,
			maxLines = 1,
			style = MaterialTheme.typography.headlineSmall
		)
		
		MainDivider()
		if (clanData != null) {
			if (clanData.clanId != null) {
				AsyncImage(
					model = clanData.emblem,
					error = painterResource(R.drawable.ic_connection_error),
					placeholder = painterResource(R.drawable.no_clan),
					contentDescription = null,
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = dimensionResource(R.dimen.padding_big))
				)
				
				Text(
					text = stringResource(R.string.created_at) + " " +
							clanData.createdAt.asStringDate("short"),
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.bodySmall
				)
				
				Text(
					text = "[" + clanData.tag + "]",
					modifier = Modifier
						.padding(
							top = dimensionResource(R.dimen.padding_big),
							bottom = dimensionResource(R.dimen.padding_extra_small)
						)
						.fillMaxWidth(),
					textAlign = TextAlign.Center,
					color = Color(clanData.color.toColorInt()),
					style = MaterialTheme.typography.headlineSmall
				)
				
				Text(
					text = clanData.name,
					modifier = Modifier
						.padding(
							top = dimensionResource(R.dimen.padding_extra_small),
							bottom = dimensionResource(R.dimen.padding_big),
						)
						.fillMaxWidth(),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.headlineSmall
				)
				
				MainDivider()
				
				DetailsCardItem(
					title = stringResource(R.string.joined_at),
					text = clanData.joinedAt.asStringDate()
				)
				
				DetailsCardItem(
					title = stringResource(R.string.role),
					text = clanData.roleLocalized
				)
			} else
				sorryText = stringResource(R.string.clan_no_clan)
		} else
			sorryText = stringResource(R.string.clan_no_data)
		
		if (sorryText.isNotEmpty()) {
			Text(
				text = sorryText,
				modifier = Modifier
					.padding(dimensionResource(R.dimen.padding_big))
					.fillMaxWidth(),
				textAlign = TextAlign.Center,
				overflow = TextOverflow.Ellipsis,
				maxLines = 1,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun DetailsClanPreview() {
	DynamicAssistantTheme {
		DetailsClanCard(
			null
//			AccountClanInfo(
//				accountId = 1,
//				joinedAt = 1691693119,
//				roleLocalized = "Private",
//				clanId = 502345049, // null,
//				createdAt = 1422654206,
//				membersCount = 42,
//				name = "We are newbies",
//				tag = "NOOB",
//				color = "#6D12A0",
//				emblem = "https://eu.wargaming.net/clans/media/clans/emblems/" +
//						"cl_042/502345049/emblem_195x195.png",
//			)
		)
	}
}