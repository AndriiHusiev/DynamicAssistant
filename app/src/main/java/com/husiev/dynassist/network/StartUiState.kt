package com.husiev.dynassist.network

import com.husiev.dynassist.network.dataclasses.NetworkStartSearchInfo

sealed interface SearchResultUiState {
	
	object Loading : SearchResultUiState
	
	/**
	 * The state query is empty or too short. To distinguish the state between the
	 * (initial state or when the search query is cleared) vs the state where no search
	 * result is returned, explicitly define the empty query state.
	 */
	object EmptyQuery : SearchResultUiState
	
	object LoadFailed : SearchResultUiState
	
	data class Success(
		val accounts: NetworkStartSearchInfo,
	) : SearchResultUiState {
		fun isEmpty(): Boolean = accounts.data?.isEmpty() ?: false
	}
	
}