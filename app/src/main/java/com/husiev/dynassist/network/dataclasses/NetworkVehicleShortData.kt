package com.husiev.dynassist.network.dataclasses

import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkVehicleShortItem(
	@SerialName(value = "tank_id")
	val tankId: Int,
	@SerialName(value = "mark_of_mastery")
	val markOfMastery: Int,
	val statistics: NetworkVehicleShortSubItem
)

@Serializable
data class NetworkVehicleShortSubItem(
	val wins: Int,
	val battles: Int,
)

fun NetworkVehicleShortItem.asEntity(accountId: Int, lastBattleTime: Int) = VehicleStatDataEntity(
	accountId = accountId,
	tankId = tankId,
	lastBattleTime = lastBattleTime,
	markOfMastery = markOfMastery,
	battles = statistics.battles,
	wins = statistics.wins,
)