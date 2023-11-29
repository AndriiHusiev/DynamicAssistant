package com.husiev.dynassist.database

import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.asEntity
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.components.start.utils.asEntity
import com.husiev.dynassist.database.entity.ClanEntity
import com.husiev.dynassist.database.entity.PlayersEntity
import com.husiev.dynassist.database.entity.VehicleShortDataEntity
import com.husiev.dynassist.database.entity.asExternalModel
import com.husiev.dynassist.network.dataclasses.NetworkAccountClanData
import com.husiev.dynassist.network.dataclasses.NetworkAccountPersonalData
import com.husiev.dynassist.network.dataclasses.NetworkAccountPersonalStatistics
import com.husiev.dynassist.network.dataclasses.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(
	private val database: AppDatabase
) {
	val listOfPlayers: Flow<List<StartAccountInfo>> = database.playersDao().loadPlayersList()
		.map { it.map(PlayersEntity::asExternalModel) }
	
	val checkedPlayers: Flow<List<StartAccountInfo>> = database.playersDao().checkedPlayers()
		.map { it.map(PlayersEntity::asExternalModel) }
	
	suspend fun addPlayer(player: StartAccountInfo) =
		database.playersDao().insert(player.asEntity())
	
	suspend fun deletePlayer(player: StartAccountInfo) =
		database.playersDao().delete(player.asEntity())
	
	fun updateTime(updateTime: String, accountId: Int) =
		database.playersDao().updateTime(updateTime, accountId)
	
	fun updateClan(clan: String?, emblem: String?, accountId: Int) =
		database.playersDao().updateClan(clan, emblem, accountId)
	
	fun loadPlayer(accountId: Int) = database.playersDao().loadPlayer(accountId)
		.map { it.asExternalModel() }
	
	suspend fun updateNotification(notification: Int, notifiedBattles: Int, accountId: Int) =
		database.playersDao().updateNotification(notification, notifiedBattles, accountId)
	
	
	suspend fun addPersonalData(accountPersonalData: AccountPersonalData) =
		database.personalDataDao().insert(accountPersonalData.asEntity())
	
	suspend fun updatePersonalData(networkData: NetworkAccountPersonalData) =
		database.personalDataDao().update(networkData.asEntity())
		
	fun getPersonalData(accountId: Int) =
		database.personalDataDao().loadPersonalData(accountId)
			.map { it.asExternalModel() }
	
	
	suspend fun addStatisticData(accountId: Int, stat: NetworkAccountPersonalStatistics) =
		database.statisticsDao().insert(stat.asEntity(accountId))
	
	fun getStatisticData(accountId: Int) = database.statisticsDao().loadStatisticsData(accountId)
	
	
	suspend fun addPlayerClanInfo(info: ClanEntity) =
		database.clanDataDao().insert(info)
	
	suspend fun updatePlayerClanInfo(clanData: NetworkAccountClanData) =
		database.clanDataDao().update(clanData.asEntity())
	
	fun getPlayerClanInfo(accountId: Int) =
		database.clanDataDao().loadClanData(accountId)
	
	suspend fun addVehiclesShortData(data: List<VehicleShortDataEntity>) =
		database.vehicleShortDao().insertOrReplace(data)
	
	fun getVehiclesShortData(accountId: Int) =
		database.vehicleShortDao().loadVehicleShortData(accountId)
	
	fun getVehicleStatData(accountId: Int) =
		database.vehicleStatDao().loadVehicleStatData(accountId)
}