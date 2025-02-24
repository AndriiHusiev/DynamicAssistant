package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.VehicleData
import com.husiev.dynassist.components.main.utils.getMainAvg
import com.husiev.dynassist.components.main.utils.toScreen

@Entity(tableName = "vehicle_short_data")
data class VehicleShortDataEntity(
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

fun VehicleShortDataEntity.asExternalModel(stat: List<VehicleStatDataEntity>): VehicleData {
	val lastStat = stat.last()
	val winRate = getMainAvg(lastStat.wins, lastStat.battles)
	return VehicleData(
		tankId = this.tankId,
		markOfMastery = lastStat.markOfMastery,
		battles = lastStat.battles,
		wins = lastStat.wins,
		winRate = winRate?: 0f,
		winRateLabel = winRate.toScreen(100f, "%"),
		lastBattleTime = lastStat.lastBattleTime?.asStringDate("short") ?: NO_DATA,
		name = this.name,
		type = this.type,
		description = this.description,
		nation = this.nation,
		urlSmallIcon = this.urlSmallIcon,
		urlBigIcon = this.urlBigIcon,
		tier = this.tier,
		priceGold = this.priceGold,
		priceCredit = this.priceCredit,
		isPremium = this.isPremium,
		isGift = this.isGift,
		isWheeled = this.isWheeled,
		stat = stat.asExternalModel()
	)
}