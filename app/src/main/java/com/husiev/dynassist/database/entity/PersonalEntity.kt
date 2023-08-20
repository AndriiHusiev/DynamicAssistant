package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.husiev.dynassist.components.main.utils.AccountPersonalData

@Entity(
	tableName = "personal",
	foreignKeys = [ForeignKey(
		entity = PlayersEntity::class,
		parentColumns = arrayOf("account_id"),
		childColumns = arrayOf("account_id"),
		onDelete = ForeignKey.CASCADE
	)]
)
data class PersonalEntity(
	@ColumnInfo(name = "account_id")
	@PrimaryKey val accountId: Int,
	val nickname: String,
	@ColumnInfo(name = "last_battle_time")
	val lastBattleTime: Int,
	@ColumnInfo(name = "created_at")
	val createdAt: Int,
	@ColumnInfo(name = "updated_at")
	val updatedAt: Int,
	@ColumnInfo(name = "logout_at")
	val logoutAt: Int,
	@ColumnInfo(name = "clan_id")
	val clanId: Int? = null,
	@ColumnInfo(name = "global_rating")
	val globalRating: Int,
)

fun PersonalEntity.asExternalModel() = AccountPersonalData(
	accountId = accountId,
	nickname = nickname,
	lastBattleTime = lastBattleTime,
	createdAt = createdAt,
	updatedAt = updatedAt,
	logoutAt = logoutAt,
	clanId = clanId,
	globalRating = globalRating,
)