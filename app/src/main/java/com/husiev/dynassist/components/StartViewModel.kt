package com.husiev.dynassist.components

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.utils.StartAccountInfo
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.network.NetworkRepository
import com.husiev.dynassist.network.SearchResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
		viewModelScope.launch(Dispatchers.IO) {
			databaseRepository.addPlayer(account)
		}
	}
	
	fun deleteAccount(account: StartAccountInfo) {
		viewModelScope.launch(Dispatchers.IO) {
			databaseRepository.deletePlayer(account)
		}
	}
	
	var searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(SEARCH_QUERY, "")
	
	var searchResult = MutableStateFlow<SearchResultUiState>(SearchResultUiState.EmptyQuery)
		private set
	
	fun onSearchTriggered(query: String) {
		viewModelScope.launch(Dispatchers.IO) {
			networkRepository.getList(query).collect {
				searchResult.value = it
			}
		}
	}
	
	fun onSearchQueryChanged(query: String) {
		savedStateHandle[SEARCH_QUERY] = query
	}
}