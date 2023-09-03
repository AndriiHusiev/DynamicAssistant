package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.husiev.dynassist.components.start.utils.StartAccountInfo

@Entity(tableName = "players")
data class PlayersEntity (
	@ColumnInfo(name = "account_id")
	@PrimaryKey val accountId: Int,
	val nickname: String,
	val clan: String? = null,
	val emblem: String? = null,
	@ColumnInfo(name = "update_time")
	val updateTime: String
)

fun PlayersEntity.asExternalModel() = StartAccountInfo(
	id = accountId,
	nickname = nickname,
	clan = clan,
	emblem = emblem,
	updateTime = updateTime,
)