package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.VehicleStatData

@Entity(
	tableName = "vehicle_stat",
	foreignKeys = [ForeignKey(
		entity = PlayersEntity::class,
		parentColumns = arrayOf("account_id"),
		childColumns = arrayOf("account_id"),
		onDelete = ForeignKey.CASCADE
	)],
	indices = [Index(value = ["account_id", "tank_id", "battles"], unique = true)]
)
data class VehicleStatDataEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	@ColumnInfo(name = "account_id")
	val accountId: Int,
	@ColumnInfo(name = "tank_id")
	val tankId: Int,
	@ColumnInfo(name = "last_battle_time")
	var lastBattleTime: Int? = null,
	
	// Overall results
	@ColumnInfo(name = "mark_of_mastery")
	val markOfMastery: Int,
	val battles: Int,
	val wins: Int,
//	val losses: Int,
//	val draws: Int,
//	val frags: Int,
//	val xp: Int,
//	@ColumnInfo(name = "survived_battles")
//	val survivedBattles: Int,
)

fun List<VehicleStatDataEntity>.asExternalModel(): List<VehicleStatData> {
	return this.map {
		VehicleStatData(
			tankId = it.tankId,
			lastBattleTime = it.lastBattleTime?.asStringDate("short") ?: NO_DATA,
			markOfMastery = it.markOfMastery,
			battles = it.battles,
			wins = it.wins,
		)
	}
}