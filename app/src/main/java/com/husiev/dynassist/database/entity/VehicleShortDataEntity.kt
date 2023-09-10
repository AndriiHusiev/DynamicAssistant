package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.VehicleShortData
import com.husiev.dynassist.components.main.utils.getMainAvg
import com.husiev.dynassist.components.main.utils.toScreen
import kotlinx.serialization.SerialName

@Entity(
	tableName = "vehicle_short_data",
	foreignKeys = [ForeignKey(
		entity = PlayersEntity::class,
		parentColumns = arrayOf("account_id"),
		childColumns = arrayOf("account_id"),
		onDelete = ForeignKey.CASCADE
	)],
	primaryKeys = ["account_id", "tank_id"]
)
data class VehicleShortDataEntity(
	@ColumnInfo(name = "account_id")
	val accountId: Int,
	@ColumnInfo(name = "tank_id")
	val tankId: Int,
	@SerialName(value = "last_battle_time")
	var lastBattleTime: Int? = null,
	@ColumnInfo(name = "mark_of_mastery")
	val markOfMastery: Int,
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
	val battles: Int,
	val wins: Int,
	var name: String? = null,
	var type: String? = null,
	var description: String? = null,
	var nation: String? = null,
	var tier: Int? = null,
)

fun List<VehicleShortDataEntity>.asExternalModel(): List<VehicleShortData> {
	return this.map {
		VehicleShortData(
			tankId = it.tankId,
			markOfMastery = it.markOfMastery,
			battles = it.battles,
			wins = it.wins,
			winRate = getMainAvg(it.wins, it.battles).toScreen(100f, "%"),
			lastBattleTime = it.lastBattleTime?.asStringDate("short") ?: NO_DATA,
			name = it.name,
			type = it.type,
			description = it.description,
			nation = it.nation,
			urlSmallIcon = it.urlSmallIcon,
			urlBigIcon = it.urlBigIcon,
			tier = it.tier,
			priceGold = it.priceGold,
			priceCredit = it.priceCredit,
			isPremium = it.isPremium,
			isGift = it.isGift,
			isWheeled = it.isWheeled,
		)
	}
}