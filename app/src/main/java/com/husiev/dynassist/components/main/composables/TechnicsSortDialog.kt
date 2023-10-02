package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.composables.SettingsDialogThemeChooserRow

@Composable
fun TechnicsSortDialog(
	sort: SortTechnics = SortTechnics.BATTLES,
	onDismiss: () -> Unit,
	onChangeSort: (sortTechnics: SortTechnics) -> Unit,
) {
	AlertDialog(
		onDismissRequest = { onDismiss() },
		title = {
			Text(
				text = stringResource(R.string.vehicle_sort_title),
				style = MaterialTheme.typography.titleLarge,
			)
		},
		text = {
			Divider()
			Column(Modifier.verticalScroll(rememberScrollState())) {
				Column(Modifier.selectableGroup()) {
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_sort_battles),
						selected = sort == SortTechnics.BATTLES,
						onClick = { onChangeSort(SortTechnics.BATTLES) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_sort_type),
						selected = sort == SortTechnics.TYPE,
						onClick = { onChangeSort(SortTechnics.TYPE) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_sort_level),
						selected = sort == SortTechnics.LEVEL,
						onClick = { onChangeSort(SortTechnics.LEVEL) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_sort_nation),
						selected = sort == SortTechnics.NATION,
						onClick = { onChangeSort(SortTechnics.NATION) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_sort_winrating),
						selected = sort == SortTechnics.WINRATING,
						onClick = { onChangeSort(SortTechnics.WINRATING) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_sort_premium),
						selected = sort == SortTechnics.PREMIUM,
						onClick = { onChangeSort(SortTechnics.PREMIUM) },
					)
				}
				Divider(Modifier.padding(top = 8.dp))
			}
		},
		confirmButton = {
			Text(
				text = stringResource(R.string.confirm_dialog_button_text),
				style = MaterialTheme.typography.labelLarge,
				color = MaterialTheme.colorScheme.primary,
				modifier = Modifier
					.padding(horizontal = 8.dp)
					.clickable { onDismiss() },
			)
		},
	)
}

enum class SortTechnics {
	BATTLES, TYPE, LEVEL, NATION, WINRATING, PREMIUM
}