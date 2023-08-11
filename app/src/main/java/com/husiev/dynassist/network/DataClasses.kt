package com.husiev.dynassist.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartSearchInfo (
	val status: String,
	val meta: MetaInfo? = null,
	val data: List<AccountInfo>? = null,
	val error: ErrorInfo? = null
)

@Serializable
data class AccountInfo (
	@SerialName(value = "account_id")
	val accountId: Int,
	val nickname: String,
)

@Serializable
data class MetaInfo (
	val count: Int,
)

@Serializable
data class ErrorInfo (
	val field: String?,
	val code: Int,
	val message: String,
	val value: String,
)