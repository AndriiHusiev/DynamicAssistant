package com.husiev.dynassist.database

import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.asEntity
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.components.start.utils.asEntity
import com.husiev.dynassist.database.entity.ClanEntity
import com.husiev.dynassist.database.entity.PlayersEntity
import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import com.husiev.dynassist.database.entity.asExternalModel
import com.husiev.dynassist.network.dataclasses.NetworkAccountClanData
import com.husiev.dynassist.network.dataclasses.NetworkAccountPersonalData
import com.husiev.dynassist.network.dataclasses.NetworkAccountPersonalStatistics
import com.husiev.dynassist.network.dataclasses.NetworkVehicleInfoItem
import com.husiev.dynassist.network.dataclasses.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(
	private val database: AppDatabase
) {
	var accountId: Int = 0
	var nickname: String = ""
	
	val listOfPlayers: Flow<List<StartAccountInfo>> = database.playersDao().loadPlayersList()
		.map { it.map(PlayersEntity::asExternalModel) }
	
	val checkedPlayers: Flow<List<StartAccountInfo>> = database.playersDao().checkedPlayers()
		.map { it.map(PlayersEntity::asExternalModel) }
	
	suspend fun addPlayer(player: StartAccountInfo) =
		database.playersDao().insert(player.asEntity())
	
	suspend fun deletePlayer(player: StartAccountInfo) =
		database.playersDao().delete(player.asEntity())
	
	fun updateTime(updateTime: String) =
		database.playersDao().updateTime(updateTime, accountId)
	
	fun updateClan(clan: String?, emblem: String?) =
		database.playersDao().updateClan(clan, emblem, accountId)
	
	fun loadPlayer() = database.playersDao().loadPlayer(accountId)
		.map { it.asExternalModel() }
	
	suspend fun updateNotification(notification: Int, notifiedBattles: Int, id: Int? = null) =
		database.playersDao().updateNotification(notification, notifiedBattles, id ?: accountId)
	
	
	suspend fun addPersonalData(accountPersonalData: AccountPersonalData) =
		database.personalDataDao().insert(accountPersonalData.asEntity())
	
	suspend fun updatePersonalData(networkData: NetworkAccountPersonalData) =
		database.personalDataDao().update(networkData.asEntity())
		
	fun getPersonalData() =
		database.personalDataDao().loadPersonalData(accountId)
			.map { it.asExternalModel() }
	
	
	suspend fun addStatisticData(
		stat: NetworkAccountPersonalStatistics,
		globalRating: Int,
		lastBattleTime: Int
	) = database.statisticsDao().insert(stat.asEntity(accountId, globalRating, lastBattleTime))
	
	fun getStatisticData(id: Int? = null) =
		database.statisticsDao().loadStatisticsData(id ?: accountId)
	
	fun getBattlesCount() = database.statisticsDao().loadLastBattlesCount(accountId)
	
	fun getGlobalRatingData() = database.statisticsDao().loadGlobalRatingData(accountId)
	
	
	suspend fun addPlayerClanInfo(info: ClanEntity) =
		database.clanDataDao().insert(info)
	
	suspend fun updatePlayerClanInfo(clanData: NetworkAccountClanData) =
		database.clanDataDao().update(clanData.asEntity())
	
	fun getPlayerClanInfo() =
		database.clanDataDao().loadClanData(accountId)
	
	suspend fun addVehiclesShortData(data: List<NetworkVehicleInfoItem>) =
		database.vehicleShortDao().insertOrReplace(data.asEntity())
	
	fun getExactVehiclesShortData(vehicles: List<Int>) =
		database.vehicleShortDao().loadExactVehicleShortData(vehicles)
	
	fun getVehiclesIds(vehicles: List<Int>) =
		database.vehicleShortDao().loadExactVehicleIds(vehicles)
	
	suspend fun addVehiclesStatData(data: List<VehicleStatDataEntity>) =
		database.vehicleStatDao().insertAll(data)
	
	fun getAllVehiclesStatData() = database.vehicleStatDao().loadAllVehiclesStatData(accountId)
}