package com.husiev.dynassist.components.main.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.toInt
import com.husiev.dynassist.components.main.utils.AccountClanInfo
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.start.composables.NotifyEnum
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.asExternalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
	private val databaseRepository: DatabaseRepository,
): ViewModel() {
	
	val accountId = databaseRepository.accountId
	
	val notifyState: StateFlow<NotifyEnum> =
		databaseRepository.loadPlayer(accountId)
			.map { it.notification }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = NotifyEnum.UNCHECKED
			)
	
	fun switchNotification(state: Boolean) {
		viewModelScope.launch(Dispatchers.IO) {
			databaseRepository.getStatisticData(accountId).first { list ->
				val battles = if (list.isEmpty()) 0 else list.last().battles
				databaseRepository.updateNotification(state.toInt(), battles, accountId)
				true
			}
		}
	}
	
	val personalData: StateFlow<AccountPersonalData?> =
		databaseRepository.getPersonalData(accountId)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
	
	val clanData: StateFlow<AccountClanInfo?> =
		databaseRepository.getPlayerClanInfo(accountId)
			.map { it.asExternalModel() }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
}