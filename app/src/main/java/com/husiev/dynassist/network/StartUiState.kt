package com.husiev.dynassist.network

import com.husiev.dynassist.network.dataclasses.AppError
import com.husiev.dynassist.network.dataclasses.NetworkAccountInfo

sealed interface SearchResultUiState {
	
	object Loading : SearchResultUiState
	
	/**
	 * The state query is empty or too short. To distinguish the state between the
	 * (initial state or when the search query is cleared) vs the state where no search
	 * result is returned, explicitly define the empty query state.
	 */
	object EmptyQuery : SearchResultUiState
	
	data class LoadFailed(val message: String) : SearchResultUiState
	
	data class Success(
		val accounts: List<NetworkAccountInfo>,
	) : SearchResultUiState {
		fun isEmpty(): Boolean = accounts.isEmpty()
	}
	
}