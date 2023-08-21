package com.husiev.dynassist.components.main.utils

import com.husiev.dynassist.database.entity.PersonalEntity

data class AccountPersonalData(
	val accountId: Int,
	val nickname: String,
	val lastBattleTime: Int = 0,
	val createdAt: Int = 0,
	val updatedAt: Int = 0,
	val logoutAt: Int = 0,
	val clanId: Int? = null,
	val globalRating: Int = 0,
)

fun AccountPersonalData.asEntity() = PersonalEntity(
	accountId = accountId,
	nickname = nickname,
	lastBattleTime = lastBattleTime,
	createdAt = createdAt,
	updatedAt = updatedAt,
	logoutAt = logoutAt,
	clanId = clanId,
	globalRating = globalRating,
)

data class AccountStatisticsData(
	val headers: List<String>,
	val items: List<Map<String, Any?>>,
	val divider: Float,
)

fun asInitial(headers: List<String>) = listOf(
	AccountStatisticsData(
		headers = headers,
		items = listOf(
			mapOf("--" to "--"),
			mapOf("--" to "--"),
			mapOf("--" to "--"),
			mapOf("--" to "--")),
		divider = 1f
	)
)