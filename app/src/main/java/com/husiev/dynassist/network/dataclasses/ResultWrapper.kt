package com.husiev.dynassist.network.dataclasses

sealed class ResultWrapper<out T> {
	data class Success<out T>(val data: T) : ResultWrapper<T>()
	data class Error(val error: AppError) : ResultWrapper<Nothing>()
}