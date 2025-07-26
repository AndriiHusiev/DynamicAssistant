package com.husiev.dynassist.database.entity

import androidx.room.*
import com.husiev.dynassist.components.main.utils.NO_DATA

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
	
	@Ignore var maxXpTank: String,
	@Ignore var maxDamageTank: String,
	@Ignore var maxFragsTank: String,
	
	@ColumnInfo(name = "global_rating", defaultValue = "0")
	val globalRating: Int,
	@ColumnInfo(name = "last_battle_time", defaultValue = "0")
	val lastBattleTime: Int,
) {
	constructor(id: Int = 0, accountId: Int, battles: Int, wins: Int, losses: Int, draws: Int,
	            frags: Int, xp: Int, survivedBattles: Int, spotted: Int, capturePoints: Int,
	            droppedCapturePoints: Int, damageDealt: Int, shots: Int, hits: Int,
	            explosionHits: Int, piercings: Int, hitsPercents: Int, damageReceived: Int,
	            directHitsReceived: Int, explosionHitsReceived: Int, noDamageDirectHitsReceived: Int,
	            piercingsReceived: Int, tankingFactor: Float, maxXp: Int, maxXpTankId: Int? = null,
	            maxDamage: Int, maxDamageTankId: Int? = null, maxFrags: Int,
	            maxFragsTankId: Int? = null, avgDamageBlocked: Float, avgDamageAssisted: Float,
	            avgDamageAssistedTrack: Float, avgDamageAssistedRadio: Float, globalRating: Int,
	            lastBattleTime: Int
	) : this (id, accountId, battles, wins, losses, draws, frags, xp, survivedBattles, spotted,
		capturePoints, droppedCapturePoints, damageDealt, shots, hits, explosionHits, piercings,
		hitsPercents, damageReceived, directHitsReceived, explosionHitsReceived,
		noDamageDirectHitsReceived, piercingsReceived, tankingFactor, maxXp, maxXpTankId,
		maxDamage, maxDamageTankId, maxFrags, maxFragsTankId, avgDamageBlocked, avgDamageAssisted,
		avgDamageAssistedTrack, avgDamageAssistedRadio, NO_DATA, NO_DATA, NO_DATA, globalRating,
		lastBattleTime
	)
}

data class StatisticsWithVehicleNames(
	@Embedded
	val statistics: StatisticsEntity?, // Включаем все поля из StatisticsEntity
	
	// Добавляем поля для названий танков
	@ColumnInfo(name = "max_xp_tank_name")
	val maxXpTankName: String?,
	
	@ColumnInfo(name = "max_damage_tank_name")
	val maxDamageTankName: String?,
	
	@ColumnInfo(name = "max_frags_tank_name")
	val maxFragsTankName: String?
)

data class GlobalRatingSubSet(
	@ColumnInfo(name = "global_rating")
	val globalRating: Int,
	@ColumnInfo(name = "last_battle_time")
	val lastBattleTime: Int,
)

data class GlobalRatingData(
	val globalRating: List<Int>,
	val lastBattleTime: List<String>,
)

fun List<GlobalRatingSubSet>.asExternalModel(): GlobalRatingData {
	val listGR = mutableListOf<Int>()
	val listLBT = mutableListOf<String>()
	
	this.forEach {
		if (it.globalRating > 0) {
			listGR.add(it.globalRating)
			listLBT.add(it.lastBattleTime.asStringDate("shortest"))
		}
	}
	
	return GlobalRatingData(listGR, listLBT)
}