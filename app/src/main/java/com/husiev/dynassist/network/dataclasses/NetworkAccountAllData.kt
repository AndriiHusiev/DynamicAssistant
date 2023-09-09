package com.husiev.dynassist.network.dataclasses

import com.husiev.dynassist.database.entity.PersonalEntity
import com.husiev.dynassist.database.entity.StatisticsEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAccountAllData(
	val status: String = "error",
	val meta: NetworkMetaInfo? = null,
	val data: Map<String, NetworkAccountPersonalData>? = null,
	val error: NetworkErrorInfo? = null
)

@Serializable
data class NetworkAccountPersonalData(
	@SerialName(value = "account_id")
	val accountId: Int,
	val nickname: String,
	@SerialName(value = "last_battle_time")
	val lastBattleTime: Int,
	@SerialName(value = "created_at")
	val createdAt: Int,
	@SerialName(value = "updated_at")
	val updatedAt: Int,
	@SerialName(value = "logout_at")
	val logoutAt: Int,
	@SerialName(value = "clan_id")
	val clanId: Int? = null,
	@SerialName(value = "global_rating")
	val globalRating: Int,
	
	val statistics: NetworkStatistics
)

fun NetworkAccountPersonalData.asEntity() = PersonalEntity(
	accountId = accountId,
	nickname = nickname,
	lastBattleTime = lastBattleTime,
	createdAt = createdAt,
	updatedAt = updatedAt,
	logoutAt = logoutAt,
	clanId = clanId,
	globalRating = globalRating,
)

@Serializable
data class NetworkStatistics(val all: NetworkAccountPersonalStatistics)

@Serializable
data class NetworkAccountPersonalStatistics(
	// Overall results
	val battles: Int,
	val wins: Int,
	val losses: Int,
	val draws: Int,
	val frags: Int,
	val xp: Int,
	@SerialName(value = "survived_battles")
	val survivedBattles: Int,
	
	// Battle Performance
	val spotted: Int,
	@SerialName(value = "capture_points")
	val capturePoints: Int,
	@SerialName(value = "dropped_capture_points")
	val droppedCapturePoints: Int,
	// Damage Dealt subsection
	@SerialName(value = "damage_dealt")
	val damageDealt: Int,
	val shots: Int,
	val hits: Int,
	@SerialName(value = "explosion_hits")
	val explosionHits: Int,
	val piercings: Int,
	@SerialName(value = "hits_percents")
	val hitsPercents: Int,
	// Damage Received subsection
	@SerialName(value = "damage_received")
	val damageReceived: Int,
	@SerialName(value = "direct_hits_received")
	val directHitsReceived: Int,
	@SerialName(value = "explosion_hits_received")
	val explosionHitsReceived: Int,
	@SerialName(value = "no_damage_direct_hits_received")
	val noDamageDirectHitsReceived: Int,
	@SerialName(value = "piercings_received")
	val piercingsReceived: Int,
	@SerialName(value = "tanking_factor")
	val tankingFactor: Float,
	
	// Record Score
	@SerialName(value = "max_xp")
	val maxXp: Int,
	@SerialName(value = "max_xp_tank_id")
	val maxXpTankId: Int? = null,
	@SerialName(value = "max_damage")
	val maxDamage: Int,
	@SerialName(value = "max_damage_tank_id")
	val maxDamageTankId: Int? = null,
	@SerialName(value = "max_frags")
	val maxFrags: Int,
	@SerialName(value = "max_frags_tank_id")
	val maxFragsTankId: Int? = null,
	
	// Average Score per Battle
	@SerialName(value = "avg_damage_blocked")
	val avgDamageBlocked: Float,
	@SerialName(value = "avg_damage_assisted")
	val avgDamageAssisted: Float,
	@SerialName(value = "avg_damage_assisted_track")
	val avgDamageAssistedTrack: Float,
	@SerialName(value = "avg_damage_assisted_radio")
	val avgDamageAssistedRadio: Float,
	
	// No need now parameters
	@SerialName(value = "battles_on_stunning_vehicles")
	val battlesOnStunningVehicles: Int,
	@SerialName(value = "stun_number")
	val stunNumber: Int,
	@SerialName(value = "stun_assisted_damage")
	val stunAssistedDamage: Int,
	@SerialName(value = "battle_avg_xp")
	val battleAvgXp: Int,
)

fun NetworkAccountPersonalStatistics.asEntity(accountId: Int) = StatisticsEntity(
	accountId = accountId,
	battles = battles,
	wins = wins,
	losses = losses,
	draws = draws,
	frags = frags,
	xp = xp,
	survivedBattles = survivedBattles,
	spotted = spotted,
	capturePoints = capturePoints,
	droppedCapturePoints = droppedCapturePoints,
	damageDealt = damageDealt,
	shots = shots,
	hits = hits,
	explosionHits = explosionHits,
	piercings = piercings,
	hitsPercents = hitsPercents,
	damageReceived = damageReceived,
	directHitsReceived = directHitsReceived,
	explosionHitsReceived = explosionHitsReceived,
	noDamageDirectHitsReceived = noDamageDirectHitsReceived,
	piercingsReceived = piercingsReceived,
	tankingFactor = tankingFactor,
	maxXp = maxXp,
	maxXpTankId = maxXpTankId,
	maxDamage = maxDamage,
	maxDamageTankId = maxDamageTankId,
	maxFrags = maxFrags,
	maxFragsTankId = maxFragsTankId,
	avgDamageBlocked = avgDamageBlocked,
	avgDamageAssisted = avgDamageAssisted,
	avgDamageAssistedTrack = avgDamageAssistedTrack,
	avgDamageAssistedRadio = avgDamageAssistedRadio,
)