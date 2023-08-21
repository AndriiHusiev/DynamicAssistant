package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.MainRoutesData
import kotlin.reflect.full.memberProperties

@Entity(
	tableName = "statistics",
	foreignKeys = [ForeignKey(
		entity = PlayersEntity::class,
		parentColumns = arrayOf("account_id"),
		childColumns = arrayOf("account_id"),
		onDelete = ForeignKey.CASCADE
	)],
	indices = [Index(value = arrayOf("account_id"))]
)
data class StatisticsEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	@ColumnInfo(name = "account_id")
	val accountId: Int,
	
	// Overall results
	val battles: Int,
	val wins: Int,
	val losses: Int,
	val draws: Int,
	val frags: Int,
	val xp: Int,
	@ColumnInfo(name = "survived_battles")
	val survivedBattles: Int,
	
	// Battle Performance
	val spotted: Int,
	@ColumnInfo(name = "capture_points")
	val capturePoints: Int,
	@ColumnInfo(name = "dropped_capture_points")
	val droppedCapturePoints: Int,
	// Damage Dealt subsection
	@ColumnInfo(name = "damage_dealt")
	val damageDealt: Int,
	val shots: Int,
	val hits: Int,
	@ColumnInfo(name = "explosion_hits")
	val explosionHits: Int,
	val piercings: Int,
	@ColumnInfo(name = "hits_percents")
	val hitsPercents: Int,
	// Damage Received subsection
	@ColumnInfo(name = "damage_received")
	val damageReceived: Int,
	@ColumnInfo(name = "direct_hits_received")
	val directHitsReceived: Int,
	@ColumnInfo(name = "explosion_hits_received")
	val explosionHitsReceived: Int,
	@ColumnInfo(name = "no_damage_direct_hits_received")
	val noDamageDirectHitsReceived: Int,
	@ColumnInfo(name = "piercings_received")
	val piercingsReceived: Int,
	@ColumnInfo(name = "tanking_factor")
	val tankingFactor: Float,
	
	// Record Score
	@ColumnInfo(name = "max_xp")
	val maxXp: Int,
	@ColumnInfo(name = "max_xp_tank_id")
	val maxXpTankId: Int? = null,
	@ColumnInfo(name = "max_damage")
	val maxDamage: Int,
	@ColumnInfo(name = "max_damage_tank_id")
	val maxDamageTankId: Int? = null,
	@ColumnInfo(name = "max_frags")
	val maxFrags: Int,
	@ColumnInfo(name = "max_frags_tank_id")
	val maxFragsTankId: Int? = null,
	
	// Average Score per Battle
	@ColumnInfo(name = "avg_damage_blocked")
	val avgDamageBlocked: Float,
	@ColumnInfo(name = "avg_damage_assisted")
	val avgDamageAssisted: Float,
	@ColumnInfo(name = "avg_damage_assisted_track")
	val avgDamageAssistedTrack: Float,
	@ColumnInfo(name = "avg_damage_assisted_radio")
	val avgDamageAssistedRadio: Float,
)

fun StatisticsEntity.asExternalModel(mrd: MainRoutesData): AccountStatisticsData {
	val items = mutableListOf<Map<String, Any?>>()

	val allMembers = mutableMapOf<String, Any?>()
	for (prop in StatisticsEntity::class.memberProperties)
		allMembers[prop.name] = prop.get(this)

	mrd.items.forEach { list ->
		val map = mutableMapOf<String, Any?>()

		list.forEach { item ->
			val (key, value) = item.split(":")
			map[value] = allMembers[key]
		}

//		for((key, value) in map)
//			logDebugOut("StatisticsEntity", "asExternalModel", "$key = $value")
		items.add(map)
	}

	return AccountStatisticsData(
		headers = mrd.headers,
		items = items,
		divider = allMembers["battles"].toString().toFloatOrNull() ?: 1f
	)
}