package com.husiev.dynassist.components.start

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.ClanEntity
import com.husiev.dynassist.network.NetworkRepository
import com.husiev.dynassist.network.SearchResultUiState
import com.husiev.dynassist.network.dataclasses.ResultWrapper
import com.husiev.dynassist.network.dataclasses.toFormattedString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_QUERY = "searchQuery"

@HiltViewModel
class StartViewModel @Inject constructor(
	private val networkRepository: NetworkRepository,
	private val savedStateHandle: SavedStateHandle,
	private val databaseRepository: DatabaseRepository
) : ViewModel() {
	
	val accounts: StateFlow<List<StartAccountInfo>> = databaseRepository.listOfPlayers
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)
	
	fun addAccount(account: StartAccountInfo) {
		viewModelScope.launch {
			databaseRepository.addPlayer(account)
			databaseRepository.addPlayerClanInfo(ClanEntity(account.id))
			databaseRepository.addPersonalData(AccountPersonalData(
				accountId = account.id,
				nickname = account.nickname,
			))
		}
	}
	
	fun deleteAccount(account: StartAccountInfo) {
		viewModelScope.launch {
			databaseRepository.deletePlayer(account)
		}
	}
	
	var searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(SEARCH_QUERY, "")
	
	private val _searchResult = MutableStateFlow<SearchResultUiState>(SearchResultUiState.EmptyQuery)
	val searchResult = _searchResult.asStateFlow()
	
	fun onSearchTriggered(query: String) {
		viewModelScope.launch {
			_searchResult.value = when(val response = networkRepository.getList(query)) {
				is ResultWrapper.Success -> SearchResultUiState.Success(response.data)
				is ResultWrapper.Error -> {
					SearchResultUiState.LoadFailed(response.error.toFormattedString())
				}
			}
		}
	}
	
	fun setSearchResult(resultUiState: SearchResultUiState) {
		_searchResult.value = resultUiState
	}
	
	fun onSearchQueryChanged(query: String) {
		savedStateHandle[SEARCH_QUERY] = query
	}
}