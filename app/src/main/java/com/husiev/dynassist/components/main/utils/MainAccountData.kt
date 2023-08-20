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
	// Overall results
	val battles: Int,
	val wins: Int,
	val losses: Int,
	val draws: Int,
	val frags: Int,
	val xp: Int,
	val survivedBattles: Int,
	
	// Battle Performance
	val spotted: Int,
	val capturePoints: Int,
	val droppedCapturePoints: Int,
	// Damage Dealt subsection
	val damageDealt: Int,
	val shots: Int,
	val hits: Int,
	val explosionHits: Int,
	val piercings: Int,
	val hitsPercents: Int,
	// Damage Received subsection
	val damageReceived: Int,
	val directHitsReceived: Int,
	val explosionHitsReceived: Int,
	val noDamageDirectHitsReceived: Int,
	val piercingsReceived: Int,
	val tankingFactor: Float,
	
	// Record Score
	val maxXp: Int,
	val maxXpTankId: Int? = null,
	val maxDamage: Int,
	val maxDamageTankId: Int? = null,
	val maxFrags: Int,
	val maxFragsTankId: Int? = null,
	
	// Average Score per Battle
	val avgDamageBlocked: Float,
	val avgDamageAssisted: Float,
	val avgDamageAssistedTrack: Float,
	val avgDamageAssistedRadio: Float,
)
