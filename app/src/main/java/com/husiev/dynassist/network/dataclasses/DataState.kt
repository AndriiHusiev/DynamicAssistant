package com.husiev.dynassist.network.dataclasses

sealed interface DataState {
	data class Success<T>(val data: T) : DataState
	data class Error(val errorMessage: String) : DataState
	object Loading : DataState
	object StandBy : DataState
}