package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_short_data")
data class VehicleInfoEntity(
	@ColumnInfo(name = "tank_id")
	@PrimaryKey val tankId: Int,
	@ColumnInfo(name = "url_small_icon")
	var urlSmallIcon: String? = null,
	@ColumnInfo(name = "url_big_icon")
	var urlBigIcon: String? = null,
	@ColumnInfo(name = "price_gold")
	var priceGold: Int? = null,
	@ColumnInfo(name = "price_credit")
	var priceCredit: Int? = null,
	@ColumnInfo(name = "is_wheeled")
	var isWheeled: Boolean? = null,
	@ColumnInfo(name = "is_premium")
	var isPremium: Boolean? = null,
	@ColumnInfo(name = "is_gift")
	var isGift: Boolean? = null,
	var name: String? = null,
	var type: String? = null,
	var description: String? = null,
	var nation: String? = null,
	var tier: Int? = null,
)