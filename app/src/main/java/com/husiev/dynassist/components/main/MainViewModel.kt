package com.husiev.dynassist.components.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.MainRoutesData
import com.husiev.dynassist.components.main.utils.Result
import com.husiev.dynassist.components.main.utils.asInitial
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.asExternalModel
import com.husiev.dynassist.network.MainNetworkState
import com.husiev.dynassist.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

private const val ACCOUNT_ID = "account_id"
private const val NICKNAME = "nickname"

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mrd: MainRoutesData,
	private val savedStateHandle: SavedStateHandle,
	private val networkRepository: NetworkRepository,
	private val databaseRepository: DatabaseRepository
): ViewModel() {
	val accountId: Int = savedStateHandle.get<Int>(ACCOUNT_ID) ?: 0
	
	val nickname: String = savedStateHandle.get<String>(NICKNAME) ?: ""
	
	init {
		getAccountAllData(accountId)
	}
	
	var queryResult = MutableStateFlow<Result<String>>(Result.Loading)
	
	val personalData: StateFlow<AccountPersonalData?> =
		databaseRepository.getPersonalData(accountId)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
	
	val statisticData: StateFlow<List<AccountStatisticsData>> =
		databaseRepository.getStatisticData(accountId)
			.map { list ->
				list.map { it.asExternalModel(mrd) }
			}
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = asInitial(mrd.headers)
			)
	
	private fun getAccountAllData(accountId: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			networkRepository.getAccountAllData(accountId).collect {
				queryResult.value = when(it) {
					is MainNetworkState.Loading -> Result.Loading
					is MainNetworkState.LoadFailed -> Result.Error()
					
					is MainNetworkState.Success -> {
						it.all.data?.get(accountId.toString())?.let { networkData ->
							databaseRepository.getStatisticData(accountId).collect { list ->
								if (list.isEmpty() || list.last().battles < networkData.statistics.all.battles) {
									databaseRepository.updatePersonalData(networkData)
									databaseRepository.addStatisticData(accountId, networkData.statistics.all)
								}
								databaseRepository.updateTime(Date().time.toString(), accountId)
							}
						}
						Result.Success(it.all.status)
					}
				}
			}
		}
	}
}