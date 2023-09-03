package com.husiev.dynassist.components.main.utils

data class AccountClanInfo(
	val accountId: Int,
	val clanId: Int? = null,
	val createdAt: Int,
	val roleLocalized: String,
	val joinedAt: Int,
	val joinedDays: Long,
	val membersCount: Int,
	val name: String,
	val tag: String,
	val color: String,
	val emblem: String
)