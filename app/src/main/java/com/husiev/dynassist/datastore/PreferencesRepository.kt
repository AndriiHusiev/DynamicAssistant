package com.husiev.dynassist.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.husiev.dynassist.components.composables.ThemeConfig
import com.husiev.dynassist.components.utils.logDebugOut
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
	private val userPreferences: DataStore<Preferences>,
) {
	val userData = userPreferences.data
		.catch { exception ->
			// dataStore.data throws an IOException when an error is encountered when reading data
			if (exception is IOException) {
				logDebugOut("PreferencesRepository", "Error reading preferences.", exception)
				emit(emptyPreferences())
			} else {
				throw exception
			}
		}.map { preferences ->
			UserPreferences (
				theme = enumValues<ThemeConfig>().firstOrNull {
					it.ordinal == preferences[PreferencesKeys.THEME]
				} ?: ThemeConfig.FOLLOW_SYSTEM
			)
		}
	
	suspend fun setTheme(theme: ThemeConfig) {
		try {
			userPreferences.edit { preferences ->
				preferences[PreferencesKeys.THEME] = theme.ordinal
			}
		} catch (exception: IOException) {
			logDebugOut("PreferencesRepository", "Failed to update user preferences", exception)
		}
	}
	
	private object PreferencesKeys {
		val THEME = intPreferencesKey("theme")
	}
}