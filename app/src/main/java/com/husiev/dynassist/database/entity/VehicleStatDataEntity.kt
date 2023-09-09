package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
	tableName = "vehicle_stat",
	foreignKeys = [ForeignKey(
		entity = PlayersEntity::class,
		parentColumns = arrayOf("account_id"),
		childColumns = arrayOf("account_id"),
		onDelete = ForeignKey.CASCADE
	)],
	primaryKeys = ["account_id", "tank_id"]
)
data class VehicleStatDataEntity(
	@ColumnInfo(name = "account_id")
	val accountId: Int,
	@ColumnInfo(name = "tank_id")
	val tankId: Int,
	
	// Overall results
	val battles: Int,
	val wins: Int,
	val losses: Int,
	val draws: Int,
	val frags: Int,
	val xp: Int,
	@ColumnInfo(name = "survived_battles")
	val survivedBattles: Int,
)

fun List<VehicleStatDataEntity>.asExternalModel(): List<Pair<Int, Int>> {
	return this.map {
		Pair(
			it.accountId,
			it.tankId,
		)
	}
}