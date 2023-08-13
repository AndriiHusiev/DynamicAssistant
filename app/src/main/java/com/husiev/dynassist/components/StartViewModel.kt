package com.husiev.dynassist.components

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.utils.StartAccountInfo
import com.husiev.dynassist.network.NetworkRepository
import com.husiev.dynassist.network.SearchResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_QUERY = "searchQuery"

@HiltViewModel
class StartViewModel @Inject constructor(
	private val networkRepository: NetworkRepository,
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
	private val _accounts = MutableStateFlow(
			mutableListOf(
			StartAccountInfo("load","DTS"), StartAccountInfo("vector"),
			StartAccountInfo("asset"), StartAccountInfo("format","KFC"),
			StartAccountInfo("modifier"), StartAccountInfo("You"),
			StartAccountInfo("png"), StartAccountInfo("logo"),
			StartAccountInfo("element"), StartAccountInfo("content","NIL"),
		)
	)
	
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
	
	val accounts: StateFlow<List<StartAccountInfo>> = _accounts
	
	fun addAccount(account: StartAccountInfo) {
		_accounts.value.add(account)
	}
}