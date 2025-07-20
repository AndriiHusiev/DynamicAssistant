package com.husiev.dynassist.components.main.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.husiev.dynassist.database.entity.PersonalEntity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class AccountPersonalData(
	val accountId: Int,
	val nickname: String,
	val lastBattleTime: String = NO_DATA,
	val createdAt: String = NO_DATA,
	val updatedAt: String = NO_DATA,
	val logoutAt: String = NO_DATA,
	val clanId: Int? = null,
	val globalRating: Int = 0,
)

fun AccountPersonalData.asEntity() = PersonalEntity(
	accountId = accountId,
	nickname = nickname,
	lastBattleTime = lastBattleTime.asInt(),
	createdAt = createdAt.asInt(),
	updatedAt = updatedAt.asInt(),
	logoutAt = logoutAt.asInt(),
	clanId = clanId,
	globalRating = globalRating,
)

fun String.asInt(): Int {
	if (this == NO_DATA) return 0
	val formatter = SimpleDateFormat(DETAILS_PATTERN, Locale.getDefault())
	val date = formatter.parse(this)
	return (date?.time?.div(1000))?.toInt() ?: 0
}

fun Int?.bigToString(): String {
	return this?.let {
		DecimalFormat.getIntegerInstance(Locale.getDefault())
			.apply { isGroupingUsed = true }
			.format(this)
	} ?: NO_DATA
}

const val DETAILS_PATTERN = "EEE, dd MMM yyy, HH:mm:ss"
const val SHORT_PATTERN = "dd MMM yyy"
const val SHORTEST_PATTERN = "dd.MM.yyyy"

/**
 * A complete set of data ready to be displayed on the UI.
 * @param title Parameter name
 * @param mainValue Average value per battle
 * ```
 * mainValue = param / battles
 * ```
 * @param auxValue An auxiliary composed parameter intended for display in the Summary Screen
 * ```
 * auxValue = "$sessionImpactValue / $sessionAvgValue"
 * ```
 * @param absValue Absolute value of the parameter
 * @param sessionAbsValue Absolute value per session
 * ```
 * sessionAbsValue = latestParam - prevParam
 * ```
 * @param sessionAvgValue Average value per session
 * ```
 * sessionAvgValue = (actualParam - prevParam) / (actualBattles - prevBattles)
 * ```
 * @param sessionImpactValue Shows how the session result affects all statistics
 * ```
 * sessionImpactValue = actualParam / actualBattles - prevParam / prevBattles
 * ```
 * @param color Shows the color of sessionImpactValue: Green / Red
 * @param imageVector An Arrow ImageVector that illustrates sessionImpactValue
 * @param comment This parameter for aux target
 * @param group Group in the Summary Screen
 */
data class FullAccStatData(
	val statId: Int,
	val title: String,
	val mainValue: String,
	val auxValue: String? = null,
	val absValue: String? = null,
	val sessionAbsValue: String? = null,
	val sessionAvgValue: String? = null,
	val sessionImpactValue: String? = null,
	val color: Color? = null,
	val imageVector: ImageVector? = null,
	val comment: String? = null,
	val group: SummaryGroup,
)

data class SingleParamData(
	val item: FullAccStatData,
	val values: List<Float>,
	val dates: List<String>,
)