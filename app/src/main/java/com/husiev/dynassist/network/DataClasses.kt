package com.husiev.dynassist.network

import com.husiev.dynassist.components.start.utils.StartAccountInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

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

fun AccountInfo.asExternalModel() = StartAccountInfo(
	id = accountId,
	nickname = nickname,
	updateTime = Date().time.toString()
)