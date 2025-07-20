package com.husiev.dynassist.components.main.utils

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.husiev.dynassist.R
import com.husiev.dynassist.database.entity.StatisticsEntity
import com.husiev.dynassist.components.main.utils.SummaryGroup.AVERAGE_SCORE
import com.husiev.dynassist.components.main.utils.SummaryGroup.BATTLE_PERFORMANCE
import com.husiev.dynassist.components.main.utils.SummaryGroup.OVERALL_RESULTS
import com.husiev.dynassist.components.main.utils.SummaryGroup.RECORD_SCORE
import com.husiev.dynassist.database.entity.StatisticsWithVehicleNames
import com.husiev.dynassist.database.entity.asStringDate
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.log10

class StatisticsUIMapper @Inject constructor(
	@param:ApplicationContext private val context: Context,
) {
	private val resources: Resources = context.resources
	private val statsToProcess = listOf(
		StatisticConfig.Precalculated(0, resources.getString(R.string.battles), OVERALL_RESULTS) { lst ->
			lst.map { it?.battles?.toFloat() } },
		StatisticConfig.Calculated(1, resources.getString(R.string.wins), OVERALL_RESULTS) { lst ->
			Pair(lst.map { it?.wins }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(2, resources.getString(R.string.losses), OVERALL_RESULTS, revertHappiness = true) { lst ->
			Pair(lst.map { it?.losses }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(3, resources.getString(R.string.draws), OVERALL_RESULTS, revertHappiness = true) { lst ->
			Pair(lst.map { it?.draws }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(4, resources.getString(R.string.frags), OVERALL_RESULTS, multiplier = 1f) { lst ->
			Pair(lst.map { it?.frags }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(5, resources.getString(R.string.xp), OVERALL_RESULTS, multiplier = 1f) { lst ->
			Pair(lst.map { it?.xp }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(6, resources.getString(R.string.survived_battles), OVERALL_RESULTS) { lst ->
			Pair(lst.map { it?.survivedBattles }, lst.map { it?.battles }) },
		
		StatisticConfig.Calculated(7, resources.getString(R.string.spotted), BATTLE_PERFORMANCE, multiplier = 1f) { lst ->
			Pair(lst.map { it?.spotted }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(8, resources.getString(R.string.capture_points), BATTLE_PERFORMANCE, multiplier = 1f) { lst ->
			Pair(lst.map { it?.capturePoints }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(9, resources.getString(R.string.dropped_capture_points), BATTLE_PERFORMANCE, multiplier = 1f) { lst ->
			Pair(lst.map { it?.droppedCapturePoints }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(10, resources.getString(R.string.damage_dealt), BATTLE_PERFORMANCE, multiplier = 1f) { lst ->
			Pair(lst.map { it?.damageDealt }, lst.map { it?.battles }) },
		StatisticConfig.Calculated(11, resources.getString(R.string.damage_received), BATTLE_PERFORMANCE, multiplier = 1f, revertHappiness = true) { lst ->
			Pair(lst.map { it?.damageReceived }, lst.map { it?.battles }) },

		StatisticConfig.RecordScore(12, resources.getString(R.string.max_xp), RECORD_SCORE) { Pair(it?.maxXpTankName, it?.statistics?.maxXp) },
		StatisticConfig.RecordScore(13, resources.getString(R.string.max_damage), RECORD_SCORE) { Pair(it?.maxDamageTankName, it?.statistics?.maxDamage) },
		StatisticConfig.RecordScore(14, resources.getString(R.string.max_frags), RECORD_SCORE) { Pair(it?.maxFragsTankName, it?.statistics?.maxFrags) },
		
		StatisticConfig.Precalculated(15, resources.getString(R.string.avg_damage_blocked), AVERAGE_SCORE) { lst ->
			lst.map { it?.avgDamageBlocked } },
		StatisticConfig.Precalculated(16, resources.getString(R.string.avg_damage_assisted), AVERAGE_SCORE) { lst ->
			lst.map { it?.avgDamageAssisted } },
		StatisticConfig.Precalculated(17, resources.getString(R.string.avg_damage_assisted_track), AVERAGE_SCORE) { lst ->
			lst.map { it?.avgDamageAssistedTrack } },
		StatisticConfig.Precalculated(18, resources.getString(R.string.avg_damage_assisted_radio), AVERAGE_SCORE) { lst ->
			lst.map { it?.avgDamageAssistedRadio } },
		
		StatisticConfig.Precalculated(19, resources.getString(R.string.global_rating), AVERAGE_SCORE, special = true) { lst ->
			lst.map { it?.globalRating?.toFloat() } },
		StatisticConfig.Precalculated(20, resources.getString(R.string.last_battle_time), AVERAGE_SCORE, special = true) { lst ->
			lst.map { it?.lastBattleTime?.toFloat() } },
	)
	
	fun getTitleById(id: Int) = statsToProcess.find { it.statId == id }?.title
	
	/**
	 * Prepares full parameters list with session impact
	 */
	fun mapToUIModel(stat: List<StatisticsWithVehicleNames>): List<FullAccStatData> {
		val list = mutableListOf<StatisticsEntity?>()
		repeat(2) { index ->
			val item = if (stat.size > index)
				stat[index].statistics
			else
				null
			list.add(item)
		}
		
		return statsToProcess.mapNotNull { config ->
			if (config is StatisticConfig.Precalculated && config.special)
				return@mapNotNull null
			
			when(config) {
				is StatisticConfig.Calculated ->
					createCalculatedItem(config, list)
				is StatisticConfig.RecordScore ->
					createMaxItem(config, stat[0])
				is StatisticConfig.Precalculated ->
					createPrecalculatedItem(config, list)
			}
		}
	}
	
	/**
	 * Prepares single parameter data with lists for Line Graph
	 */
	fun mapSingleToUIModel(
		stat: List<StatisticsEntity>,
		selected: Int
	): SingleParamData? {
		val config = statsToProcess.find { it.statId == selected }
		
		return createCalculatedFullItem(config, stat)
	}
	
	private fun createCalculatedItem(
		config: StatisticConfig.Calculated,
		list: List<StatisticsEntity?>,
	): FullAccStatData {
		val (param, battles) = config.valueExtractor(list)
		val latestParam = param[0]
		val prevParam = param[1]
		val latestBattles = battles[0]
		val prevBattles = battles[1]
		val suffix = if (config.multiplier == 100f) "%" else ""
		
		val sessionImpactValue = getImpactSession(latestParam, prevParam, latestBattles, prevBattles)
		val sessionAvgValue = getAvgSession(latestParam, prevParam, latestBattles, prevBattles)
			.toScreen(config.multiplier, suffix)
			
		return FullAccStatData(
			statId = config.statId,
			title = config.title,
			mainValue = getMainAvg(latestParam, latestBattles).toScreen(config.multiplier, suffix),
			auxValue = sessionImpactValue.toScreen(config.multiplier, suffix) + " / " + sessionAvgValue,
			absValue = getAbsValue(latestParam),
			sessionAbsValue = getAbsSessionDiff(latestParam, prevParam),
			sessionAvgValue = sessionAvgValue,
			sessionImpactValue = sessionImpactValue.toScreen(
				multiplier = config.multiplier,
				suffix = suffix,
				showPlus = true,
				forceToAll = true
			),
			color = sessionImpactValue.happyColor(config.revertHappiness),
			imageVector = sessionImpactValue.happyIcon(),
			group = config.group
		)
	}
	
	private fun createMaxItem(
		config: StatisticConfig.RecordScore,
		latest: StatisticsWithVehicleNames?,
	): FullAccStatData {
		val (vehicleName, maxValue) = config.valueExtractor(latest)
		
		return FullAccStatData(
			statId = config.statId,
			title = config.title,
			mainValue = getAbsValue(maxValue),
			comment = vehicleName,
			group = config.group,
		)
	}
	
	private fun createPrecalculatedItem(
		config: StatisticConfig.Precalculated,
		list: List<StatisticsEntity?>,
	): FullAccStatData {
		val param = config.valueExtractor(list)
		val latestParam = param[0]
		val prevParam = param[1]
		val sessionImpactValue = getImpactSession(latestParam, prevParam)
		
		return FullAccStatData(
			statId = config.statId,
			title = config.title,
			mainValue = getAbsValue(latestParam),
			auxValue = sessionImpactValue.toScreen(1f, showPlus = true),
			sessionImpactValue = sessionImpactValue.toScreen(1f, showPlus = true),
			color = sessionImpactValue.happyColor(false),
			imageVector = sessionImpactValue.happyIcon(),
			group = config.group,
		)
	}
	
	private fun createCalculatedFullItem(
		config: StatisticConfig?,
		stat: List<StatisticsEntity>,
	): SingleParamData? {
		val list = mutableListOf<StatisticsEntity?>()
		repeat(2) { index ->
			val item = if (stat.size > index)
				stat[stat.size - index - 1]
			else
				null
			list.add(item)
		}
		
		return when(config) {
			is StatisticConfig.Calculated -> {
				val fullAccStatData = createCalculatedItem(config, list)
				val (param, battles) = config.valueExtractor(stat)
				val values = calcFloatList(param, battles, config.multiplier)
				val dates = stat.map { it.lastBattleTime.asStringDate("shortest") }
				SingleParamData(fullAccStatData, values, dates)
			}
			is StatisticConfig.Precalculated -> {
				val fullAccStatData = createPrecalculatedItem(config, list)
				val param = config.valueExtractor(stat).mapNotNull { it }
				val dates = stat.map { it.lastBattleTime.asStringDate("shortest") }
				SingleParamData(fullAccStatData, param, dates)
			}
			else -> null
		}
	}
}

sealed interface StatisticConfig {
	val statId: Int
	val title: String
	val group: SummaryGroup
	
	data class Calculated(
		override val statId: Int,
		override val title: String,
		override val group: SummaryGroup,
		val multiplier: Float = 100f,
		val revertHappiness: Boolean = false,
		val valueExtractor: (List<StatisticsEntity?>) -> Pair<List<Int?>, List<Int?>>,
	) : StatisticConfig
	
	data class RecordScore(
		override val statId: Int,
		override val title: String,
		override val group: SummaryGroup,
		val valueExtractor: (StatisticsWithVehicleNames?) -> Pair<String?, Int?>,
	) : StatisticConfig
	
	data class Precalculated(
		override val statId: Int,
		override val title: String,
		override val group: SummaryGroup,
		val special: Boolean = false,
		val valueExtractor: (List<StatisticsEntity?>) -> List<Float?>
	) : StatisticConfig
}

enum class SummaryGroup(@param:StringRes val titleResId: Int) {
	OVERALL_RESULTS(R.string.summary_header_overall),
	BATTLE_PERFORMANCE(R.string.summary_header_performance),
	RECORD_SCORE(R.string.summary_header_record),
	AVERAGE_SCORE(R.string.summary_header_average)
}

fun getMainAvg(actualParam: Number?, actualBattles: Int?): Float? {
	val param = actualParam?.toFloat() ?: 1f
	return when(val battles = actualBattles?.toFloat()) {
		is Float -> if (battles == 0f) 0f else param / battles
		else -> null
	}
}

fun getAbsValue(param: Number?): String {
	return when(param) {
		null -> NO_DATA
		else -> param.toFloat().toScreen(1f, forceToInt = param is Int)
	}
}

fun getImpactSession(actualParam: Number?, prevParam: Number?, actualBattles: Int?, prevBattles: Int?): Float? {
	return if (actualParam != null && prevParam != null) {
		val part1 = actualParam.toFloat() / (actualBattles?.toFloat() ?: 1f)
		val part2 = prevParam.toFloat() / (prevBattles?.toFloat() ?: 1f)
		part1 - part2
	} else null
}

fun getImpactSession(actualParam: Float?, prevParam: Float?): Float? {
	return if (actualParam != null && prevParam != null) {
		actualParam - prevParam
	} else null
}

fun getAvgSession(actualParam: Number?, prevParam: Number?, actualBattles: Int?, prevBattles: Int?): Float? {
	return if (actualParam != null && prevParam != null) {
		val part1 = actualParam.toFloat() - prevParam.toFloat()
		val part2 = (actualBattles?.toFloat() ?: 2f) - (prevBattles?.toFloat() ?: 1f)
		part1 / part2
	} else null
}

fun getAbsSessionDiff(actualParam: Number?, prevParam: Number?): String {
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

fun calcFloatList(param: List<Int?>, battles: List<Int?>, multiplier: Float = 100f): List<Float> {
	val list = mutableListOf<Float>()
	param.forEachIndexed { index, item ->
		list.add(multiplier * item!! / battles[index]!!)
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
	var exp = getPureDecimalExponent(this)
	if (forceToAll) return (exp - 2).absoluteValue
	if (forceToInt) exp = 10
	return when(exp) {
		in 2..38 -> 0
		1 -> 1
		-1, 0 -> (exp - 2).absoluteValue
		-2 -> (exp - 1).absoluteValue
		else -> exp.absoluteValue
	}
}

/**
 * Возвращает десятичную экспоненту. Например, для числа 508.55 экспонента равна +2,
 * т.к. в экспоненциальном виде оно выглядит как 5.085500e+02.
 *
 * Десятичная экспонента E для числа N (где 1 <= M < 10) в форме N = M * 10^E
 * равна floor(log10(|N|))
 */
fun getPureDecimalExponent(number: Float): Int {
	return when {
		!number.isFinite() || number == 0f -> 0
		else -> floor(log10(number.absoluteValue.toDouble())).toInt()
	}
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