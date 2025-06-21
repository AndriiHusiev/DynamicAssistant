package com.husiev.dynassist.components.main.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.husiev.dynassist.database.entity.PersonalEntity
import com.husiev.dynassist.database.entity.StatisticsEntity
import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.reflect.full.memberProperties

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

data class AccountStatisticsData(
	val title: String,
	val mainValue: String,
	val auxValue: String?,
	val absValue: String?,
	val sessionAbsValue: String?,
	val sessionAvgValue: String?,
	val sessionImpactValue: String?,
	val color: Color?,
	val imageVector: ImageVector?,
	val values: List<Float>?,
	val comment: String? = null,
)

data class SingleParamData(
	val item: AccountStatisticsData,
	val dates: List<String>,
)

/**
 * Transformation for vehicle statistic.
 */
fun List<VehicleStatDataEntity>.asStatisticModel(): List<AccountStatisticsData> {
	val items = arrayOf<Int?>(null, null, null, null, null, null)
	if (this.isNotEmpty()) {
		items[0] = this[this.size-1].wins
		items[2] = this[this.size-1].battles
		items[4] = this[this.size-1].lastBattleTime
		if (this.size > 1) {
			items[1] = this[this.size-2].wins
			items[3] = this[this.size-2].battles
			items[5] = this[this.size-1].lastBattleTime
		}
	}
	val allMembers = listOf(
		mapOf("wins" to items[0], "battles" to items[2], "lastBattleTime" to items[4]),
		mapOf("wins" to items[1], "battles" to items[3], "lastBattleTime" to items[5]),
	)
	
	return listOf(
		reducedStatItem(
			tag = "battles",
			items = mapOf("battles" to "Battles"),
			allMembers = allMembers,
			values = this.map { it.battles.toFloat() },
		),
		fullStatItem(
			tag = "wins",
			items = mapOf("wins" to "Wins"),
			allMembers = allMembers,
			values = calcFloatList(
				this.map { it.wins.toFloat() },
				this.map { it.battles.toFloat() }
			),
		),
		reducedStatItem(
			tag = "lastBattleTime",
			items = mapOf("lastBattleTime" to "Last Battle Time"),
			allMembers = allMembers,
			values = this.map { it.lastBattleTime?.toFloat() ?: 0f },
		),
	)
}

/**
 * Transformation for account statistic.
 */
fun List<StatisticsEntity>.asExternalModel(mrd: MainRoutesData): Map<String, List<AccountStatisticsData>> {
	val allMembers = mutableListOf<Map<String, Any?>>()
	val allValues = mutableMapOf<String, List<Float>>()
	
	repeat(2) { index ->
		val map = mutableMapOf<String, Any?>()
		for (prop in StatisticsEntity::class.memberProperties) {
			map[prop.name] = if (this.size > index)
				prop.get(this[this.size - index - 1])
			else
				null
		}
		allMembers.add(map)
	}
	
	for (prop in StatisticsEntity::class.memberProperties) {
		val values = mutableListOf<Float>()
		for (i in this.indices) {
			values.add(prop.get(this[i]).toString().toFloatOrNull() ?: 0f)
		}
		allValues[prop.name] = values
	}
	
	val map = mutableMapOf<String, List<AccountStatisticsData>>()
	
	map[mrd.headers[0]] = listOf(
		reducedStatItem("battles", mrd.items, allMembers, allValues["battles"]),
		fullStatItem("wins", mrd.items, allMembers, calcFloatList(allValues["wins"], allValues["battles"])),
		fullStatItem("losses", mrd.items, allMembers, calcFloatList(allValues["losses"], allValues["battles"]), revertHappiness = true),
		fullStatItem("draws", mrd.items, allMembers, calcFloatList(allValues["draws"], allValues["battles"]), revertHappiness = true),
		fullStatItem("frags", mrd.items, allMembers, calcFloatList(allValues["frags"], allValues["battles"], 1f), multiplier = 1f),
		fullStatItem("xp", mrd.items, allMembers, calcFloatList(allValues["xp"], allValues["battles"], 1f), multiplier = 1f),
		fullStatItem("survivedBattles", mrd.items, allMembers, calcFloatList(allValues["survivedBattles"], allValues["battles"])),
	)
	
	map[mrd.headers[1]] = listOf(
		fullStatItem("spotted", mrd.items, allMembers, calcFloatList(allValues["spotted"], allValues["battles"], 1f), multiplier = 1f),
		fullStatItem("capturePoints", mrd.items, allMembers, calcFloatList(allValues["capturePoints"], allValues["battles"], 1f), multiplier = 1f),
		fullStatItem("droppedCapturePoints", mrd.items, allMembers, calcFloatList(allValues["droppedCapturePoints"], allValues["battles"], 1f), multiplier = 1f),
		fullStatItem("damageDealt", mrd.items, allMembers, calcFloatList(allValues["damageDealt"], allValues["battles"], 1f), multiplier = 1f),
		fullStatItem("damageReceived", mrd.items, allMembers, calcFloatList(allValues["damageReceived"], allValues["battles"], 1f), multiplier = 1f, revertHappiness = true),
	)
	
	map[mrd.headers[2]] = listOf(
		reducedStatItem("maxXp", mrd.items, allMembers, allValues["maxXp"], "maxXpTank"),
		reducedStatItem("maxDamage", mrd.items, allMembers, allValues["maxDamage"], "maxDamageTank"),
		reducedStatItem("maxFrags", mrd.items, allMembers, allValues["maxFrags"], "maxFragsTank"),
	)
	
	map[mrd.headers[3]] = listOf(
		reducedStatItem("avgDamageBlocked", mrd.items, allMembers, allValues["avgDamageBlocked"]),
		reducedStatItem("avgDamageAssisted", mrd.items, allMembers, allValues["avgDamageAssisted"]),
		reducedStatItem("avgDamageAssistedTrack", mrd.items, allMembers, allValues["avgDamageAssistedTrack"]),
		reducedStatItem("avgDamageAssistedRadio", mrd.items, allMembers, allValues["avgDamageAssistedRadio"]),
	)
	
	map[mrd.headers[4]] = listOf(
		reducedStatItem("globalRating", mrd.items, allMembers, allValues["globalRating"]),
		reducedStatItem("lastBattleTime", mrd.items, allMembers, allValues["lastBattleTime"]),
	)
	
	return map
}

private fun reducedStatItem(
	tag: String,
	items: Map<String, String>,
	allMembers: List<Map<String, Any?>>,
	values: List<Float>?,
	comment: String? = null,
): AccountStatisticsData {
	val mainValue = getAbsValue(allMembers[0][tag])
	
	return AccountStatisticsData(
		title = items[tag] ?: "",
		mainValue = mainValue,
		auxValue = null,
		absValue = mainValue,
		sessionAbsValue = getAbsSessionDiff(allMembers[0][tag], allMembers[1][tag]),
		sessionAvgValue = null,
		sessionImpactValue = null,
		color = null,
		imageVector = null,
		values = values,
		comment = comment?.let { allMembers[0][it].toString() },
	)
}

private fun fullStatItem(
	tag: String,
	items: Map<String, String>,
	allMembers: List<Map<String, Any?>>,
	values: List<Float>?,
	multiplier: Float = 100f,
	revertHappiness: Boolean = false,
): AccountStatisticsData {
	val suffix = if (multiplier == 100f) "%" else ""
	
	val mainValue = getMainAvg(
		allMembers[0][tag],
		allMembers[0]["battles"],
	).toScreen(multiplier, suffix)
	
	val auxProgressValue = getAuxProgress(
		allMembers[0][tag],
		allMembers[1][tag],
		allMembers[0]["battles"],
		allMembers[1]["battles"],
	)
	
	val sessionImpactValue = auxProgressValue.toScreen(
		multiplier = multiplier,
		suffix = suffix,
		showPlus = true,
		forceToAll = true
	)
	
	val sessionAvgValue = getAvgSession(
		allMembers[0][tag],
		allMembers[1][tag],
		allMembers[0]["battles"],
		allMembers[1]["battles"],
	).toScreen(multiplier, suffix)
	
	return AccountStatisticsData(
		title = items[tag] ?: "",
		mainValue = mainValue,
		auxValue = auxProgressValue.toScreen(multiplier, suffix) + " / " + sessionAvgValue,
		absValue = getAbsValue(allMembers[0][tag]),
		sessionAbsValue = getAbsSessionDiff(allMembers[0][tag], allMembers[1][tag]),
		sessionAvgValue = sessionAvgValue,
		sessionImpactValue = sessionImpactValue,
		color = auxProgressValue.happyColor(revertHappiness),
		imageVector = auxProgressValue.happyIcon(),
		values = values
	)
}

fun getMainAvg(actualParam: Any?, actualBattles: Any?): Float? {
	val param = actualParam.toString().toFloatOrNull() ?: 1f
	return when(val battles = actualBattles.toString().toFloatOrNull()) {
		is Float -> if (battles == 0f) 0f else param / battles
		else -> null
	}
}

fun getAuxProgress(actualParam: Any?, prevParam: Any?, actualBattles: Any?, prevBattles: Any?): Float? {
	return if (actualParam is Int && prevParam is Int) {
		val part1 = actualParam.toFloat() / (actualBattles.toString().toFloatOrNull() ?: 1f)
		val part2 = prevParam.toFloat() / (prevBattles.toString().toFloatOrNull() ?: 1f)
		part1 - part2
	} else null
}

fun getAvgSession(actualParam: Any?, prevParam: Any?, actualBattles: Any?, prevBattles: Any?): Float? {
	return if (actualParam is Int && prevParam is Int) {
		val part1 = actualParam - prevParam
		val part2 = (actualBattles.toString().toFloatOrNull() ?: 2f) -
				(prevBattles.toString().toFloatOrNull() ?: 1f)
		part1.toFloat() / part2
	} else null
}

fun getAbsValue(param: Any?): String {
	return when(param) {
		null -> NO_DATA
		else -> param.toString().toFloat().toScreen(1f, forceToInt = param is Int)
	}
}

fun getAbsSessionDiff(actualParam: Any?, prevParam: Any?): String {
	return if (actualParam is Int && prevParam is Int) {
		val calc = (actualParam - prevParam).toFloat()
		calc.toScreen(
			multiplier = 1f,
			showPlus = true,
			forceToInt = true
		)
	} else if (actualParam is Float && prevParam is Float) {
		(actualParam - prevParam).toScreen(
			multiplier = 1f,
			showPlus = true
		)
	} else NO_DATA
}

fun calcFloatList(param: List<Float>?, battles: List<Float>?, multiplier: Float = 100f): List<Float> {
	val list = mutableListOf<Float>()
	if (param != null && battles != null) {
		param.forEachIndexed { index, fl ->
			list.add(multiplier * fl / battles[index])
		}
	}
	return list
}

fun Float?.toScreen(
	multiplier: Float,
	suffix: String = "",
	showPlus: Boolean = false,
	forceToInt: Boolean = false,
	forceToAll: Boolean = false,
): String {
	val calc = this?.let { multiplier * it }
	return calc?.let {
		val prefix = if (showPlus && it > 0) "+" else ""
		prefix + String.format(Locale.getDefault(), calc.format(forceToInt, forceToAll), calc) + suffix
	} ?: NO_DATA
}

fun Float.format(forceToInt: Boolean, forceToAll: Boolean = false): String =
	"%,.${this.exp(forceToInt, forceToAll)}f"

fun Float.exp(forceToInt: Boolean, forceToAll: Boolean = false): Int {
	var exp = getPureExponent(this)
	if (forceToAll) return (exp - 2).absoluteValue
	if (forceToInt) exp = 10
	return when(exp) {
		in 3..38 -> 0
		1, 2 -> 1//(exp - 1).absoluteValue
		-1, 0 -> (exp - 2).absoluteValue
		else -> 3
	}
}

fun getPureExponent(number: Float): Int {
	val (_, exponent) = String.format(null, "%e", number).split("e")
	return exponent.toInt()
}

fun Float?.happyIcon(): ImageVector? {
	return if (this == null)
		null
	else if (this == 0f)
		Icons.Filled.DragHandle
	else if (this < 0)
		Icons.Filled.ArrowDropDown
	else
		Icons.Filled.ArrowDropUp
}

fun Float?.happyColor(revert: Boolean): Color? {
	return if (this == null)
		null
	else if (this == 0f)
		Color.Gray
	else if ((this < 0) == revert)
		Color(0xFF009688)
	else
		Color(0xFFE91E63)
}

const val NO_DATA = "--"