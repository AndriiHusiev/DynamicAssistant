package com.husiev.dynassist.network

sealed interface MainNetworkState {
	
	object Loading : MainNetworkState
	
	object LoadFailed : MainNetworkState
	
	data class Success(val all: NetworkAccountAllData) : MainNetworkState
}