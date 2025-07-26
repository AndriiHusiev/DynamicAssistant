package com.husiev.dynassist.components.main.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.husiev.dynassist.R
import com.husiev.dynassist.database.entity.VehicleInfoEntity

data class ReducedVehicleData(
	val tankId: Int,
	val markOfMastery: Int,
	val battles: Int,
	val winRate: Float,
	val lastBattleTime: String,
	val name: String? = null,
	val type: String? = null,
	val nation: String? = null,
	val urlBigIcon: String? = null,
	val tier: Int? = null,
	val isPremium: Boolean? = null,
	val isGift: Boolean? = null,
	val isWheeled: Boolean? = null,
	val stat: FullAccStatData,
) : Comparable<ReducedVehicleData> {
	override fun compareTo(other: ReducedVehicleData): Int {
		return compareValuesBy(this, other, { it.battles }, { it.type }, { it.tier }, { it.nation }, { it.winRate }, { it.isPremium })
	}
}

data class VehicleData(
	val info: VehicleInfoEntity,
	val ui: VehicleUiData,
	val victories: List<Float>,
	val dates: List<String>,
)

data class VehicleUiData(
	val battles: String,
	val victories: String,
	val winRate: String,
	val sessionAvgValue: String,
	val sessionImpactValue: String,
	val markOfMastery: Int,
	val color: Color?,
	val imageVector: ImageVector?,
)

fun masteryToResId(markOfMastery: Int) = when(markOfMastery) {
	1 -> R.drawable.ic_step_mark_3
	2 -> R.drawable.ic_step_mark_2
	3 -> R.drawable.ic_step_mark_1
	4 -> R.drawable.ic_step_mark_0
	else -> R.drawable.ic_step_mark_4
}

fun flagToResId(nation: String?) = when(nation) {
	"ussr" -> R.drawable.flag_ussr
	"germany" -> R.drawable.flag_germany
	"usa" -> R.drawable.flag_usa
	"china" -> R.drawable.flag_china
	"czech" -> R.drawable.flag_czech
	"france" -> R.drawable.flag_france
	"italy" -> R.drawable.flag_italy
	"japan" -> R.drawable.flag_japan
	"poland" -> R.drawable.flag_poland
	"uk" -> R.drawable.flag_uk
	"sweden" -> R.drawable.flag_sweden
	else -> R.drawable.flag_empty
}

fun symbolToResId(nation: String?) = when(nation) {
	"ussr" -> R.drawable.ic_nations_ussr
	"germany" -> R.drawable.ic_nations_germany
	"usa" -> R.drawable.ic_nations_usa
	"china" -> R.drawable.ic_nations_china
	"czech" -> R.drawable.ic_nations_czech
	"france" -> R.drawable.ic_nations_france
	"italy" -> R.drawable.ic_nations_italy
	"japan" -> R.drawable.ic_nations_japan
	"poland" -> R.drawable.ic_nations_poland
	"uk" -> R.drawable.ic_nations_uk
	"sweden" -> R.drawable.ic_nations_sweden
	else -> R.drawable.ic_nations_empty
}

fun roleToResId(role: String?) = when(role) {
	"lightTank" -> R.drawable.role_tank_light
	"mediumTank" -> R.drawable.role_tank_medium
	"heavyTank" -> R.drawable.role_tank_heavy
	"AT-SPG" -> R.drawable.role_tank_atspg
	"SPG" -> R.drawable.role_tank_spg
	else -> R.drawable.role_tank_none
}

fun tierToResId(tier: Int?) = when(tier) {
	1 -> R.drawable.level_1
	2 -> R.drawable.level_2
	3 -> R.drawable.level_3
	4 -> R.drawable.level_4
	5 -> R.drawable.level_5
	6 -> R.drawable.level_6
	7 -> R.drawable.level_7
	8 -> R.drawable.level_8
	9 -> R.drawable.level_9
	10 -> R.drawable.level_10
	else -> R.drawable.level_none
}

fun String?.roleResId() = when(this) {
	"lightTank" -> R.string.vehicle_role_light
	"mediumTank" -> R.string.vehicle_role_medium
	"heavyTank" -> R.string.vehicle_role_heavy
	"AT-SPG" -> R.string.vehicle_role_atspg
	"SPG" -> R.string.vehicle_role_spg
	else -> R.string.no_data
}