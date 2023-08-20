package com.husiev.dynassist.network

import com.husiev.dynassist.components.start.utils.StartAccountInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NetworkStartSearchInfo (
	val status: String,
	val meta: NetworkMetaInfo? = null,
	val data: List<NetworkAccountInfo>? = null,
	val error: NetworkErrorInfo? = null
)

@Serializable
data class NetworkAccountInfo (
	@SerialName(value = "account_id")
	val accountId: Int,
	val nickname: String,
)

@Serializable
data class NetworkMetaInfo (
	val count: Int,
)

@Serializable
data class NetworkErrorInfo (
	val field: String?,
	val code: Int,
	val message: String,
	val value: String,
)

fun NetworkAccountInfo.asExternalModel() = StartAccountInfo(
	id = accountId,
	nickname = nickname,
	updateTime = Date().time.toString()
)