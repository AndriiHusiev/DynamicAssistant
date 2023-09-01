package com.husiev.dynassist.database.entity

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.husiev.dynassist.components.main.utils.AccountClanInfo
import com.husiev.dynassist.components.main.utils.NO_DATA

@Entity(
	tableName = "clan",
	foreignKeys = [ForeignKey(
		entity = PlayersEntity::class,
		parentColumns = arrayOf("account_id"),
		childColumns = arrayOf("account_id"),
		onDelete = ForeignKey.CASCADE
	)]
)
data class ClanEntity(
	@ColumnInfo(name = "account_id")
	@PrimaryKey val accountId: Int,
	@ColumnInfo(name = "clan_id")
	val clanId: Int? = null,
	@ColumnInfo(name = "created_at")
	val createdAt: Int = 0,
	@ColumnInfo(name = "joined_at")
	val joinedAt: Int = 0,
	@ColumnInfo(name = "members_count")
	val membersCount: Int = 0,
	@ColumnInfo(name = "role_localized")
	val roleLocalized: String = NO_DATA,
	val name: String = NO_DATA,
	val tag: String = NO_DATA,
	val color: String = String.format("#%06X", (0xFFFFFF and Color.Gray.toArgb())),
	val emblem: String = NO_DATA
)

fun ClanEntity.asExternalModel() = AccountClanInfo(
	accountId = accountId,
	clanId = clanId,
	createdAt = createdAt,
	joinedAt = joinedAt,
	membersCount = membersCount,
	roleLocalized = roleLocalized,
	name = name,
	tag = tag,
	color = color,
	emblem = emblem,
)