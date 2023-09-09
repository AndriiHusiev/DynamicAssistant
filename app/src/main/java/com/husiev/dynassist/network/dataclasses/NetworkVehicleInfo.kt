package com.husiev.dynassist.network.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkVehicleInfo(
	val status: String,
	val meta: NetworkMetaInfo? = null,
	val data: Map<String, NetworkVehicleInfoItem?>? = null,
	val error: NetworkErrorInfo? = null
)

@Serializable
data class NetworkVehicleInfoItem(
	@SerialName(value = "tank_id")
	val tankId: Int,
	@SerialName(value = "is_wheeled")
	val isWheeled: Boolean,
	@SerialName(value = "is_gift")
	val isGift: Boolean,
	@SerialName(value = "is_premium")
	val isPremium: Boolean,
	@SerialName(value = "short_name")
	val shortName: String,
	@SerialName(value = "price_credit")
	val priceCredit: Int?,
	@SerialName(value = "price_gold")
	val priceGold: Int?,
	val tier: Int,
	val tag: String,
	val type: String,
	val name: String,
	val nation: String,
	val description: String,
	val images: NetworkVehicleInfoImages,
)

@Serializable
data class NetworkVehicleInfoImages(
	@SerialName(value = "small_icon")
	val smallIcon: String,
	@SerialName(value = "contour_icon")
	val contourIcon: String,
	@SerialName(value = "big_icon")
	val bigIcon: String,
)