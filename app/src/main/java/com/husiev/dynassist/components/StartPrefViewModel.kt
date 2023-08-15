package com.husiev.dynassist.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.composables.ThemeConfig
import com.husiev.dynassist.datastore.PreferencesRepository
import com.husiev.dynassist.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartPrefViewModel @Inject constructor(
	private val preferencesRepository: PreferencesRepository
): ViewModel() {
	
	val uiState: StateFlow<UserPreferences> = preferencesRepository.userData.map {
		it
	}.stateIn(
		scope = viewModelScope,
		initialValue = UserPreferences(ThemeConfig.FOLLOW_SYSTEM),
		started = SharingStarted.WhileSubscribed(5_000),
	)
	
	fun setTheme(theme: ThemeConfig) {
		viewModelScope.launch(Dispatchers.IO) {
			preferencesRepository.setTheme(theme)
		}
	}
}