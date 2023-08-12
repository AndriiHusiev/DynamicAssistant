package com.husiev.dynassist.components.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.husiev.dynassist.R


@Composable
fun SettingsDialog(
	theme: ThemeConfig = ThemeConfig.FOLLOW_SYSTEM,
	onDismiss: () -> Unit,
	onChangeTheme: (themeConfig: ThemeConfig) -> Unit,
) {
	val configuration = LocalConfiguration.current
	
	/**
	 * usePlatformDefaultWidth = false is use as a temporary fix to allow
	 * height recalculation during recomposition. This, however, causes
	 * Dialog's to occupy full width in Compact mode. Therefore max width
	 * is configured below. This should be removed when there's fix to
	 * https://issuetracker.google.com/issues/221643630
	 */
	AlertDialog(
		properties = DialogProperties(usePlatformDefaultWidth = false),
		modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
		onDismissRequest = { onDismiss() },
		title = {
			Text(
				text = stringResource(R.string.theme_settings_title),
				style = MaterialTheme.typography.titleLarge,
			)
		},
		text = {
			Divider()
			Column {
				Column(Modifier.selectableGroup()) {
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.theme_config_system_default),
						selected = theme == ThemeConfig.FOLLOW_SYSTEM,
						onClick = { onChangeTheme(ThemeConfig.FOLLOW_SYSTEM) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.theme_config_light),
						selected = theme == ThemeConfig.LIGHT,
						onClick = { onChangeTheme(ThemeConfig.LIGHT) },
					)
					SettingsDialogThemeChooserRow(
						text = stringResource(R.string.theme_config_dark),
						selected = theme == ThemeConfig.DARK,
						onClick = { onChangeTheme(ThemeConfig.DARK) },
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

@Composable
fun SettingsDialogThemeChooserRow(
	text: String,
	selected: Boolean,
	onClick: () -> Unit,
) {
	Row(
		Modifier
			.fillMaxWidth()
			.selectable(
				selected = selected,
				role = Role.RadioButton,
				onClick = onClick,
			)
			.padding(12.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		RadioButton(
			selected = selected,
			onClick = null,
		)
		Spacer(Modifier.width(8.dp))
		Text(text)
	}
}

enum class ThemeConfig {
	FOLLOW_SYSTEM, LIGHT, DARK
}