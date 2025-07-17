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
import com.husiev.dynassist.components.start.utils.logDebugOut
import com.husiev.dynassist.database.entity.StatisticsWithVehicleNames
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.log10

class StatisticsUIMapper @Inject constructor(
	@param:ApplicationContext private val context: Context,
) {
	private val resources: Resources = context.resources
	private val statsToProcess = listOf(
		StatisticConfig.Calculated(R.string.wins, OVERALL_RESULTS) { it?.wins },
		StatisticConfig.Calculated(R.string.losses, OVERALL_RESULTS, revertHappiness = true) { it?.losses },
		StatisticConfig.Calculated(R.string.draws, OVERALL_RESULTS, revertHappiness = true) { it?.draws },
		StatisticConfig.Calculated(R.string.frags, OVERALL_RESULTS, multiplier = 1f) { it?.frags },
		StatisticConfig.Calculated(R.string.xp, OVERALL_RESULTS, multiplier = 1f) { it?.xp },
		StatisticConfig.Calculated(R.string.survived_battles, OVERALL_RESULTS) { it?.survivedBattles },
		
		StatisticConfig.Calculated(R.string.spotted, BATTLE_PERFORMANCE, multiplier = 1f) { it?.spotted },
		StatisticConfig.Calculated(R.string.capture_points, BATTLE_PERFORMANCE, multiplier = 1f) { it?.capturePoints },
		StatisticConfig.Calculated(R.string.dropped_capture_points, BATTLE_PERFORMANCE, multiplier = 1f) { it?.droppedCapturePoints },
		StatisticConfig.Calculated(R.string.damage_dealt, BATTLE_PERFORMANCE, multiplier = 1f) { it?.damageDealt },
		StatisticConfig.Calculated(R.string.damage_received, BATTLE_PERFORMANCE, multiplier = 1f, revertHappiness = true) { it?.damageReceived },

		StatisticConfig.RecordScore(R.string.max_xp, RECORD_SCORE) { Pair(it?.maxXpTankName, it?.statistics?.maxXp) },
		StatisticConfig.RecordScore(R.string.max_damage, RECORD_SCORE) { Pair(it?.maxDamageTankName, it?.statistics?.maxDamage) },
		StatisticConfig.RecordScore(R.string.max_frags, RECORD_SCORE) { Pair(it?.maxFragsTankName, it?.statistics?.maxFrags) },
		
		StatisticConfig.Precalculated(R.string.avg_damage_blocked, AVERAGE_SCORE) { it?.avgDamageBlocked },
		StatisticConfig.Precalculated(R.string.avg_damage_assisted, AVERAGE_SCORE) { it?.avgDamageAssisted },
		StatisticConfig.Precalculated(R.string.avg_damage_assisted_track, AVERAGE_SCORE) { it?.avgDamageAssistedTrack },
		StatisticConfig.Precalculated(R.string.avg_damage_assisted_radio, AVERAGE_SCORE) { it?.avgDamageAssistedRadio },
	)
	
	fun mapToUIModel(stat: List<StatisticsWithVehicleNames>): List<ReducedAccStatData> {
		val list = mutableListOf<StatisticsWithVehicleNames?>()
		repeat(2) { index ->
			val item = if (stat.size > index)
				stat[index]
			else
				null
			list.add(item)
		}
		
		val latest = list[0]?.statistics
		val previous = list[1]?.statistics
		
		return statsToProcess.map { config ->
			when(config) {
				is StatisticConfig.Calculated ->
					createCalculatedItem(config, latest, previous)
				is StatisticConfig.RecordScore ->
					createMaxItem(config, stat[0])
				is StatisticConfig.Precalculated ->
					createPrecalculatedItem(config, latest, previous)
				is StatisticConfig.CalculatedSingle ->
					createStub(config)
			}
		}
	}
	
	fun mapSingleToUIModel(
		stat: List<StatisticsWithVehicleNames>,
		selectedId: String
	): SingleParamData {
		/*val list = mutableListOf<StatisticsEntity>()
		repeat(maxOf(stat.size, 2)) { index ->
			val item = if (stat.size > index)
				stat[index].statistics
			else
				null
			list.add(item)
		}*/
		val list = stat.mapNotNull { it.statistics }
		
		return createCalculatedFullItem(
			list = list,
			config = StatisticConfig.CalculatedSingle(
				titleResId = R.string.wins,
				group = OVERALL_RESULTS,
				valueExtractor = {
					Pair(emptyList(), emptyList())
				}
			),
		)
	}
	
	private fun createCalculatedItem(
		config: StatisticConfig.Calculated,
		latest: StatisticsEntity?,
		previous: StatisticsEntity?,
	): ReducedAccStatData {
		val latestParam = config.valueExtractor(latest)
		val prevParam = config.valueExtractor(previous)
		val latestBattles = latest?.battles
		val prevBattles = previous?.battles
		val suffix = if (config.multiplier == 100f) "%" else ""
		
		val auxProgressValue = getAuxProgress(latestParam, prevParam, latestBattles, prevBattles)
		val sessionAvgValue = getAvgSession(latestParam, prevParam, latestBattles, prevBattles)
			.toScreen(config.multiplier, suffix)
			
		return ReducedAccStatData(
			title = resources.getString(config.titleResId),
			mainValue = getMainAvg(latestParam, prevParam).toScreen(config.multiplier, suffix),
			auxValue = auxProgressValue.toScreen(config.multiplier, suffix) + " / " + sessionAvgValue,
//			absValue = getAbsValue(latestParam),
			sessionAbsValue = getAbsSessionDiff(latestParam, prevParam),
//			sessionAvgValue = sessionAvgValue,
//			sessionImpactValue = auxProgressValue.toScreen(
//				multiplier = config.multiplier,
//				suffix = suffix,
//				showPlus = false,
//				forceToInt = true,
//				forceToAll = true
//			),
			color = auxProgressValue.happyColor(false),
			imageVector = auxProgressValue.happyIcon(),
//			values = listOf(prevParam.toFloat(), latestParam.toFloat()),
			comment = null,
			group = config.group
		)
	}
	
	private fun createMaxItem(
		config: StatisticConfig.RecordScore,
		latest: StatisticsWithVehicleNames?,
	): ReducedAccStatData {
		val (vehicleName, maxValue) = config.valueExtractor(latest)
		
		return ReducedAccStatData(
			title = resources.getString(config.titleResId),
			mainValue = getAbsValue(maxValue),
			auxValue = null,
			sessionAbsValue = null,
			color = null,
			imageVector = null,
			comment = vehicleName,
			group = config.group,
		)
	}
	
	private fun createPrecalculatedItem(
		config: StatisticConfig.Precalculated,
		latest: StatisticsEntity?,
		previous: StatisticsEntity?,
	): ReducedAccStatData {
		val latestParam = config.valueExtractor(latest)
		val prevParam = config.valueExtractor(previous)
		
		return ReducedAccStatData(
			title = resources.getString(config.titleResId),
			mainValue = getAbsValue(latestParam),
			auxValue = null,
			sessionAbsValue = getAbsSessionDiff(latestParam, prevParam),
			color = null,
			imageVector = null,
			comment = null,
			group = config.group,
		)
	}
	
	private fun createStub(config: StatisticConfig.CalculatedSingle) = ReducedAccStatData(
		title = "",
		mainValue = "",
		auxValue = null,
		sessionAbsValue = null,
		color = null,
		imageVector = null,
		group = config.group
	)
	
	private fun createCalculatedFullItem(
		config: StatisticConfig.CalculatedSingle,
		list: List<StatisticsEntity>,
	): SingleParamData {
		val (param, battles) = config.valueExtractor(list)
		
		val fullAccStatData = FullAccStatData(
			title = "",
			mainValue = "",
			auxValue = "",
			absValue = "",
			sessionAbsValue = "",
			sessionAvgValue = "",
			sessionImpactValue = "",
			color = null,
			imageVector = null,
			comment = null,
			group = config.group
		)
		
		return SingleParamData(
			item = fullAccStatData,
			values = emptyList(),
			dates = emptyList()
		)
	}
}

sealed interface StatisticConfig {
	val titleResId: Int
	val group: SummaryGroup
	
	data class Calculated(
		@param:StringRes override val titleResId: Int,
		override val group: SummaryGroup,
		val multiplier: Float = 100f,
		val revertHappiness: Boolean = false,
		val valueExtractor: (StatisticsEntity?) -> Int?,
	) : StatisticConfig
	
	data class RecordScore(
		@param:StringRes override val titleResId: Int,
		override val group: SummaryGroup,
		val valueExtractor: (StatisticsWithVehicleNames?) -> Pair<String?, Int?>,
	) : StatisticConfig
	
	data class Precalculated(
		@param:StringRes override val titleResId: Int,
		override val group: SummaryGroup,
		val valueExtractor: (StatisticsEntity?) -> Float?
	) : StatisticConfig
	
	data class CalculatedSingle(
		@param:StringRes override val titleResId: Int,
		override val group: SummaryGroup,
		val valueExtractor: (List<StatisticsEntity>) -> Pair<List<Number?>, List<Int?>>,
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

fun getAuxProgress(actualParam: Number?, prevParam: Number?, actualBattles: Int?, prevBattles: Int?): Float? {
	return if (actualParam != null && prevParam != null) {
		val part1 = actualParam.toFloat() / (actualBattles?.toFloat() ?: 1f)
		val part2 = prevParam.toFloat() / (prevBattles?.toFloat() ?: 1f)
		part1 - part2
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
	logDebugOut("StatisticsUIMapper", "getPureDecimalExponent", "${getPureDecimalExponent(number)} -- ${exponent.toInt()}")
	return exponent.toInt()
}

fun getPureDecimalExponent(number: Float): Int {
	if (number == 0f) {
		return 0 // Экспонента 0 для числа 0.0 (или можно выбрать другое поведение, например, бросить исключение)
	}
	if (number.isNaN() || number.isInfinite()) {
		// Определите, как вы хотите обрабатывать NaN и Infinity.
		// Возможно, возвращать 0, бросать исключение или какое-то специальное значение.
		// Для данной задачи, где требуется 'Int' и 'Float?', я бы предложил null или 0.
		return 0 // Или можно изменить возвращаемый тип на Int?
	}
	
	// Десятичная экспонента E для числа N (где 1 <= M < 10) в форме N = M * 10^E
	// равна floor(log10(|N|))
	// log10 из kotlin.math работает с Double, поэтому сначала преобразуем Float в Double
	return log10(number.absoluteValue.toDouble()).toInt()
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