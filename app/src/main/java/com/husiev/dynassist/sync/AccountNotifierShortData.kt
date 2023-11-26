package com.husiev.dynassist.sync

data class AccountNotifierShortData(
	val accountId: Int,
	val nickname: String,
	val battlesCollapsed: String,
	val battlesExpanded: String,
	val winRate: String,
	val lastBattleTime: String,
)
