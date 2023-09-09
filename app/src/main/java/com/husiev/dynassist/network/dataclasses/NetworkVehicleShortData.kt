package com.husiev.dynassist.network.dataclasses

import com.husiev.dynassist.database.entity.VehicleShortDataEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkVehicleShortData(
	val status: String,
	val meta: NetworkMetaInfo? = null,
	val data: Map<String, List<NetworkVehicleShortItem>>? = null,
	val error: NetworkErrorInfo? = null
)

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

fun NetworkVehicleShortItem.asEntity(accountId: Int) = VehicleShortDataEntity(
	accountId = accountId,
	tankId = tankId,
	markOfMastery = markOfMastery,
	battles = statistics.battles,
	wins = statistics.wins,
)