package com.husiev.dynassist.database.entity

import androidx.room.Embedded
import com.husiev.dynassist.components.main.utils.*

data class FlatVehicleStatAndInfo(
	@Embedded
	val info: VehicleInfoEntity,
	
	@Embedded(prefix = "stat_")
	val stat: VehicleStatDataEntity
)

fun List<FlatVehicleStatAndInfo>.asUiTechnicModel(): List<ReducedVehicleData> {
	return this.groupBy { it.info.tankId }
		.map { (_, list) ->
			val info = list[0].info
			val statistic = list.map { it.stat }
			val lastStat = statistic.last()
			val statList = mutableListOf<VehicleStatDataEntity?>()
			repeat(2) { index ->
				val statItem = if (statistic.size > index)
					statistic[statistic.size - index - 1]
				else
					null
				statList.add(statItem)
			}
			
			ReducedVehicleData(
				tankId = info.tankId,
				name = info.name,
				type = info.type,
				nation = info.nation,
				urlBigIcon = info.urlBigIcon,
				tier = info.tier,
				isPremium = info.isPremium,
				isGift = info.isGift,
				isWheeled = info.isWheeled,
				markOfMastery = lastStat.markOfMastery,
				battles = lastStat.battles,
				lastBattleTime = lastStat.lastBattleTime?.asStringDate("short") ?: NO_DATA,
				winRate = getMainAvg(lastStat.wins, lastStat.battles)?: 0f,
				stat = createVehicleReducedItem(statList)
			)
		}
}

private fun createVehicleReducedItem(
	list: List<VehicleStatDataEntity?>,
): FullAccStatData {
	val multiplier = 100f
	val suffix = "%"
	val lastStat = list[0]
	val prevStat = list[1]
	val latestParam = lastStat?.wins
	val latestBattles = lastStat?.battles
	val prevParam = prevStat?.wins
	val prevBattles = prevStat?.battles
	val sessionImpactValue = getImpactSession(latestParam, prevParam, latestBattles, prevBattles)
	val battlesAbs = getAbsValue(latestBattles)
	val battlesSession = getAbsSessionDiff(latestBattles, prevBattles)
	val auxValue = if (battlesSession != NO_DATA) "$battlesSession / $battlesAbs"
	else battlesAbs
	
	return FullAccStatData(
		statId = 0,
		title = "",
		mainValue = getMainAvg(latestParam, latestBattles).toScreen(multiplier, suffix),
		auxValue = auxValue,
		sessionImpactValue = sessionImpactValue.toScreen(
			multiplier = multiplier,
			suffix = suffix,
			showPlus = true,
		),
		color = sessionImpactValue.happyColor(false),
		imageVector = sessionImpactValue.happyIcon(),
		group = SummaryGroup.OVERALL_RESULTS
	)
}

fun List<FlatVehicleStatAndInfo>.asUiSingleTechnicModel(): VehicleData {
	val statistic = this.map { it.stat }
	val stat2Items = mutableListOf<VehicleStatDataEntity?>()
	val wins = statistic.map { it.wins }
	val battles = statistic.map { it.battles }
	repeat(2) { index ->
		val statItem = if (statistic.size > index)
			statistic[statistic.size - index - 1]
		else
			null
		stat2Items.add(statItem)
	}
	
	return VehicleData(
		info = this[0].info,
		ui = createVehicleFullItem(stat2Items),
		victories = calcFloatList(wins, battles, 100f),
		dates = statistic.map { it.lastBattleTime?.asStringDate("shortest") ?: "" }
	)
}

private fun createVehicleFullItem(
	list: List<VehicleStatDataEntity?>,
): VehicleUiData {
	val multiplier = 100f
	val suffix = "%"
	val lastStat = list[0]
	val prevStat = list[1]
	val latestParam = lastStat?.wins
	val latestBattles = lastStat?.battles
	val prevParam = prevStat?.wins
	val prevBattles = prevStat?.battles
	
	// Battles
	val battlesAbs = getAbsValue(latestBattles)
	val battlesSession = getAbsSessionDiff(latestBattles, prevBattles)
	val battlesUi = if (battlesSession != NO_DATA)
		"$battlesSession / $battlesAbs"
	else
		battlesAbs
	
	// Victories
	val victoriesAbs = getAbsValue(latestParam)
	val victoriesSession = getAbsSessionDiff(latestParam, prevParam)
	val victoriesUi = if (victoriesSession != NO_DATA)
		"$victoriesSession / $victoriesAbs"
	else
		victoriesAbs
	
	// Other parameters
	val winRateUi = getMainAvg(latestParam, latestBattles).toScreen(multiplier, suffix)
	val sessionImpactValue = getImpactSession(latestParam, prevParam, latestBattles, prevBattles)
	val sessionAvgValue = getAvgSession(latestParam, prevParam, latestBattles, prevBattles)
		.toScreen(multiplier, suffix)
	
	return VehicleUiData(
		battles = battlesUi,
		victories = victoriesUi,
		winRate = winRateUi,
		sessionAvgValue = sessionAvgValue,
		sessionImpactValue = sessionImpactValue.toScreen(
			multiplier = multiplier,
			suffix = suffix,
			showPlus = true,
			forceToAll = true
		),
		markOfMastery = lastStat?.markOfMastery ?: 0,
		color = sessionImpactValue.happyColor(false),
		imageVector = sessionImpactValue.happyIcon(),
	)
}
