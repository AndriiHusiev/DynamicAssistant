package com.husiev.dynassist.components.main.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.husiev.dynassist.database.entity.PersonalEntity
import com.husiev.dynassist.database.entity.StatisticsEntity
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.reflect.full.memberProperties

data class AccountPersonalData(
	val accountId: Int,
	val nickname: String,
	val lastBattleTime: Int = 0,
	val createdAt: Int = 0,
	val updatedAt: Int = 0,
	val logoutAt: Int = 0,
	val clanId: Int? = null,
	val globalRating: Int = 0,
)

fun AccountPersonalData.asEntity() = PersonalEntity(
	accountId = accountId,
	nickname = nickname,
	lastBattleTime = lastBattleTime,
	createdAt = createdAt,
	updatedAt = updatedAt,
	logoutAt = logoutAt,
	clanId = clanId,
	globalRating = globalRating,
)

fun List<StatisticsEntity>.asExternalModel(mrd: MainRoutesData): List<AccountStatisticsData> {
	val summaryItems = mutableListOf<AccountStatisticsData>()
	val allMembers = mutableListOf<Map<String, Any?>>()
	val battles = mutableListOf<Float>()
	
	repeat(2) { index ->
		val map = mutableMapOf<String, Any?>()
		for (prop in StatisticsEntity::class.memberProperties) {
			map[prop.name] = if (this.size > index)
				prop.get(this[this.size - index - 1])
			else
				null
		}
		allMembers.add(map)
		battles.add(map["battles"].toString().toFloatOrNull() ?: 1f)
	}
	
	mrd.item.forEach { item ->
		val (variable, caption) = item.split(":")
		val actParam = allMembers[0][variable]
		val prevParam = allMembers[1][variable]
		
		summaryItems.add(AccountStatisticsData(
			tag = variable,
			title = caption,
			avgValue = getAvg(actParam, battles[0]),
			avgSessionValue = getAvgDiff(actParam, prevParam, battles[0], battles[1]),
			avgSessionProgress = getAvgDiffProgress(actParam, prevParam, battles[0], battles[1]),
			absValue = getAbs(actParam),
			absSessionValue = getAbsDiff(actParam, prevParam),
		))
	}
	
	return summaryItems
}

data class AccountStatisticsData(
	val tag: String,
	val title: String,
	val avgValue: Float?,
	val avgSessionValue: Float?,
	val avgSessionProgress: Float?,
	val absValue: Float?,
	val absSessionValue: Float?,
)

fun getAvg(actualParam: Any?, actualBattles: Float): Float? {
	return when(actualParam) {
		is Int -> if (actualParam == 0) 0f else actualParam.toFloat() / actualBattles
		else -> null
	}
}

fun getAvgDiff(actualParam: Any?, prevParam: Any?, actualBattles: Float, prevBattles: Float): Float? {
	return if (actualParam is Int && prevParam is Int) {
		val part1 = actualParam - prevParam
		val part2 = actualBattles - prevBattles
		part1.toFloat() / part2
	} else null
}

fun getAvgDiffProgress(actualParam: Any?, prevParam: Any?, actualBattles: Float, prevBattles: Float): Float? {
	return if (actualParam is Int && prevParam is Int) {
		val part1 = actualParam.toFloat() / actualBattles
		val part2 = prevParam.toFloat() / prevBattles
		part1 - part2
	} else null
}

fun getAbs(param: Any?): Float? {
	return when(param) {
		is Int -> param.toFloat()
		is Float -> param
		else -> null
	}
}

fun getAbsDiff(actualParam: Any?, prevParam: Any?): Float? {
	return if (actualParam is Int && prevParam is Int) {
		(actualParam - prevParam).toFloat()
	} else null
}

fun Float?.toScreen(
	multiplier: Float,
	suffix: String = "",
	showPlus: Boolean = false,
	forceToInt: Boolean = false
): String {
	val calc = this?.let { multiplier * it }
	return calc?.let {
		val prefix = if (showPlus && it > 0) "+" else ""
		prefix + String.format(Locale.getDefault(), calc.format(forceToInt), calc) + suffix
	} ?: NO_DATA
}

fun Float.format(forceToInt: Boolean): String = "%.${this.exp(forceToInt)}f"

fun Float.exp(forceToInt: Boolean): Int {
	val (mantissa, exponent) = String.format(null, "%e", this).split("e")
	var exp = exponent.toInt()
	if (forceToInt) exp = 10
	return when(exp) {
		in 3..38 -> 0
		1, 2 -> (exp - 1).absoluteValue
		-1, 0 -> (exp - 2).absoluteValue
		else -> 3
	}
}

fun Float?.happyIcon(): ImageVector {
	return if (this == null || this == 0f)
		Icons.Filled.DragHandle
	else if (this < 0)
		Icons.Filled.ArrowDropDown
	else
		Icons.Filled.ArrowDropUp
}

fun Float?.happyColor(revert: Boolean): Color {
	return if (this == null || this == 0f)
		Color.Gray
	else if ((this < 0) == revert)
		Color(0xFF009688)
	else
		Color(0xFFE91E63)
}


private const val NO_DATA = "--"