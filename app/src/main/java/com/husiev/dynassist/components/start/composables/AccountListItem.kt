package com.husiev.dynassist.components.start.composables

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.components.start.utils.asDateTime
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import java.util.Date

@Composable
fun AccountListItem(
	account: StartAccountInfo,
	modifier: Modifier = Modifier,
	onClick: (StartAccountInfo) -> Unit = {},
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
			.clickable(onClick = { onClick(account) }),
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
				AsyncImage(
					model = account.emblem,
					error = painterResource(R.drawable.no_clan),
					placeholder = painterResource(R.drawable.no_clan),
					contentDescription = null,
					modifier = Modifier
						.padding(
							horizontal = dimensionResource(R.dimen.padding_small),
							vertical = dimensionResource(R.dimen.padding_medium)
						)
						.sizeIn(
							maxWidth = 48.dp,
							maxHeight = 48.dp,
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
					
					Row(modifier = Modifier
						.padding(top = dimensionResource(R.dimen.padding_extra_small)),
						verticalAlignment = Alignment.CenterVertically
					) {
						if (account.notification == NotifyEnum.UPDATES_AVAIL) {
							NotificationDot(
								color = MaterialTheme.colorScheme.tertiary,
								modifier = Modifier
									.padding(start = dimensionResource(R.dimen.padding_small))
									.size(dimensionResource(R.dimen.padding_small)),
							)
						}
						Text(
							text = stringResource(R.string.last_update_text) +
									": ${account.updateTime.asDateTime()}",
							modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small)),
							style = MaterialTheme.typography.labelSmall,
						)
					}
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
							onClick(account)
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

@Composable
fun NotificationDot(
	color: Color,
	modifier: Modifier = Modifier,
) {
	val description = stringResource(R.string.description_new_data_dot)
	Canvas(
		modifier = modifier
			.semantics { contentDescription = description },
		onDraw = {
			drawCircle(
				color,
				radius = size.minDimension / 2,
			)
		},
	)
}

enum class NotifyEnum {
	UNCHECKED, NO_UPDATES, UPDATES_AVAIL
}

@Preview(showBackground = true)
@Preview(showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AccountListItemPreview() {
	DynamicAssistantTheme {
		Surface(
			color = MaterialTheme.colorScheme.background
		) {
			Column {
				val dateTime = Date().time.toString()
				AccountListItem(
					StartAccountInfo(1, "eater", "KFC", null, dateTime, notifiedBattles = 0),
				)
				AccountListItem(
					StartAccountInfo(
						1,
						"player",
						"NPE",
						null,
						dateTime,
						NotifyEnum.UPDATES_AVAIL,
						0
					),
				)
			}
		}
	}
}