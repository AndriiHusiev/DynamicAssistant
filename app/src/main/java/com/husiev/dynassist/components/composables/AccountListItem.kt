package com.husiev.dynassist.components.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.husiev.dynassist.R
import com.husiev.dynassist.components.utils.StartAccountInfo
import com.husiev.dynassist.components.utils.asDateTime
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import java.util.Date

@Composable
fun AccountListItem(
	account: StartAccountInfo,
	modifier: Modifier = Modifier,
	clanEmblem: Painter = painterResource(id = R.drawable.no_clan),
	onClick: (Int) -> Unit = {},
	onDelete: (StartAccountInfo) -> Unit = {},
) {
	var showMenu by remember { mutableStateOf(false) }
	
	ElevatedCard(
		modifier = Modifier
			.padding(
				horizontal = dimensionResource(R.dimen.padding_small),
				vertical = dimensionResource(R.dimen.padding_extra_small)
			)
			.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium)))
			.clickable(onClick = { onClick(account.id) }),
		shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
		elevation = CardDefaults.elevatedCardElevation(
			dimensionResource(R.dimen.padding_extra_small)
		)
	) {
		Row(
			modifier = modifier
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Row(
				modifier = Modifier.weight(1f, false),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Image(
					painter = clanEmblem,
					contentDescription = null,
					modifier = Modifier.padding(
						horizontal = dimensionResource(R.dimen.padding_small),
						vertical = dimensionResource(R.dimen.padding_medium)
					)
				)
				Column {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							text = account.nickname,
							modifier = Modifier
								.weight(1f, false)
								.padding(start = dimensionResource(R.dimen.padding_small)),
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
							)
						}
					}
					
					Text(
						text = stringResource(R.string.last_update_text) +
								": ${account.updateTime.asDateTime()}",
						modifier = Modifier
							.padding(
								start = dimensionResource(R.dimen.padding_small),
								top = dimensionResource(R.dimen.padding_extra_small),
							),
						style = MaterialTheme.typography.labelSmall,
					)
				}
			}
			
			Box {
				IconButton(
					onClick = { showMenu = true },
				) {
					Icon(
						imageVector = Icons.Filled.MoreVert,
						contentDescription = stringResource(R.string.description_start_menu_text)
					)
				}
				
				DropdownMenu(
					expanded = showMenu,
					onDismissRequest = { showMenu = false },
				) {
					DropdownMenuItem(
						text = { Text(stringResource(R.string.open_text)) },
						onClick = {
							onClick(account.id)
							showMenu = false
						},
						leadingIcon = {
							Icon(Icons.Filled.PlayArrow, contentDescription = null)
						}
					)
					DropdownMenuItem(
						text = { Text(stringResource(R.string.delete_text)) },
						onClick = {
							onDelete(account)
							showMenu = false
						},
						leadingIcon = {
							Icon(Icons.Filled.Delete, contentDescription = null)
						}
					)
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Preview(showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AccountListItemPreview() {
	DynamicAssistantTheme {
		val dateTime = Date().time.toString()
		AccountListItem(
			StartAccountInfo(1, "eater","KFC", dateTime),
		)
	}
}