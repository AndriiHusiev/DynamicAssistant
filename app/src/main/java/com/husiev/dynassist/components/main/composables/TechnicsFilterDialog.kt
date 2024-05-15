package com.husiev.dynassist.components.main.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.composables.SettingsDialogThemeChooserRow

@Composable
fun TechnicsFilterDialog(
	filter: FilterTechnics = FilterTechnics.ALL,
	onDismiss: () -> Unit,
	onChangeFilter: (filterTechnics: FilterTechnics) -> Unit,
) {
	AlertDialog(
		onDismissRequest = { onDismiss() },
		title = {
			Text(
				text = stringResource(R.string.vehicle_filter_title),
				style = MaterialTheme.typography.titleLarge,
			)
		},
		text = {
			HorizontalDivider()
			Column(Modifier.verticalScroll(rememberScrollState())) {
				Column(Modifier.selectableGroup()) {
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_filter_all),
						selected = filter == FilterTechnics.ALL,
						onClick = { onChangeFilter(FilterTechnics.ALL) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_filter_last),
						selected = filter == FilterTechnics.LAST,
						onClick = { onChangeFilter(FilterTechnics.LAST) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_filter_light),
						selected = filter == FilterTechnics.LIGHT,
						onClick = { onChangeFilter(FilterTechnics.LIGHT) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_filter_medium),
						selected = filter == FilterTechnics.MEDIUM,
						onClick = { onChangeFilter(FilterTechnics.MEDIUM) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_filter_heavy),
						selected = filter == FilterTechnics.HEAVY,
						onClick = { onChangeFilter(FilterTechnics.HEAVY) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_filter_atspg),
						selected = filter == FilterTechnics.ATSPG,
						onClick = { onChangeFilter(FilterTechnics.ATSPG) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.vehicle_filter_spg),
						selected = filter == FilterTechnics.SPG,
						onClick = { onChangeFilter(FilterTechnics.SPG) },
					)
				}
				HorizontalDivider(Modifier.padding(top = 8.dp))
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

enum class FilterTechnics(val alias: String) {
	ALL(alias = "all"),
	LAST(alias = "last"),
	LIGHT(alias = "lightTank"),
	MEDIUM(alias = "mediumTank"),
	HEAVY(alias = "heavyTank"),
	ATSPG(alias = "AT-SPG"),
	SPG(alias = "SPG")
}