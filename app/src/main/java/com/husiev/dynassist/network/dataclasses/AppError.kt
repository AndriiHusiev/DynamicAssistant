package com.husiev.dynassist.network.dataclasses

sealed interface AppError {
	data class ApiError(
		val field: String?,
		val code: Int,
		val message: String,
		val value: String
	) : AppError
	
	data class NetworkError(val cause: Throwable) : AppError
	
	data class UnknownError(val cause: Throwable) : AppError
}

fun AppError.toFormattedString(): String = when(this) {
	is AppError.UnknownError -> cause.message ?: ""
	is AppError.NetworkError -> cause.message ?: ""
	is AppError.ApiError -> buildString {
		val parts = listOfNotNull(
			field?.let { "field: $it" },
			"message: $message",
			"code: $code",
			"value: $value"
		)
		
		append(parts.joinToString(separator = "\n"))
	}
}