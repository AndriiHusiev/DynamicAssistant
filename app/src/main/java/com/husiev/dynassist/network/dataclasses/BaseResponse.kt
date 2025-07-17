package com.husiev.dynassist.network.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class BaseResponse(
	val status: String = "error",
	val meta: NetworkMetaInfo? = null,
	val data: JsonElement? = null,
	val error: NetworkErrorInfo? = null
)

@Serializable
data class NetworkMetaInfo (
	val count: Int,
	@SerialName(value = "page_total")
	val pageTotal: Int? = null,
	val total: Int? = null,
	val limit: Int? = null,
	val page: Int? = null,
)

@Serializable
data class NetworkErrorInfo (
	val field: String?,
	val code: Int,
	val message: String,
	val value: String,
)

fun NetworkErrorInfo.asApiError() = AppError.ApiError(
	field = this.field,
	code = this.code,
	message = this.message,
	value = this.value
)