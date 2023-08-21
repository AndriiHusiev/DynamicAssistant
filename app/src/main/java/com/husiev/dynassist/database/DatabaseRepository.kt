package com.husiev.dynassist.database

import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.asEntity
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.components.start.utils.asEntity
import com.husiev.dynassist.database.entity.PlayersEntity
import com.husiev.dynassist.database.entity.asExternalModel
import com.husiev.dynassist.network.NetworkAccountPersonalData
import com.husiev.dynassist.network.NetworkAccountPersonalStatistics
import com.husiev.dynassist.network.asEntity
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
	
	suspend fun addPlayer(player: StartAccountInfo) =
		database.playersDao().insert(player.asEntity())
	
	suspend fun deletePlayer(player: StartAccountInfo) =
		database.playersDao().delete(player.asEntity())
	
	suspend fun updateTime(updateTime: String, accountId: Int) =
		database.playersDao().updateTime(updateTime, accountId)
	
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
}