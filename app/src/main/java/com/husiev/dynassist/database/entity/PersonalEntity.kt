package com.husiev.dynassist.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.DETAILS_PATTERN
import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.components.main.utils.SHORT_PATTERN
import java.text.SimpleDateFormat
import java.util.Locale

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
	lastBattleTime = lastBattleTime.asStringDate(),
	createdAt = createdAt.asStringDate(),
	updatedAt = updatedAt.asStringDate(),
	logoutAt = logoutAt.asStringDate(),
	clanId = clanId,
	globalRating = globalRating,
)

fun Int.asStringDate(type: String = "full"): String {
	if (this == 0) return NO_DATA
	val pattern = when(type) {
		"full" -> DETAILS_PATTERN
		else -> SHORT_PATTERN
	}
	val formatter = SimpleDateFormat(pattern, Locale.getDefault())
	return formatter.format(this * 1000L)
}