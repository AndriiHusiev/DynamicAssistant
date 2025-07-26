package com.husiev.dynassist.network.dataclasses

import com.husiev.dynassist.components.main.secure
import com.husiev.dynassist.database.entity.VehicleInfoEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

fun List<NetworkVehicleInfoItem>.asEntity(): List<VehicleInfoEntity> {
	return this.map {
		VehicleInfoEntity(
			tankId = it.tankId,
			urlSmallIcon = it.images.smallIcon.secure(),
			urlBigIcon = it.images.bigIcon.secure(),
			priceGold = it.priceGold,
			priceCredit = it.priceCredit,
			isWheeled = it.isWheeled,
			isPremium = it.isPremium,
			isGift = it.isGift,
			name = it.name,
			type = it.type,
			description = it.description,
			nation = it.nation,
			tier = it.tier
		)
	}
}