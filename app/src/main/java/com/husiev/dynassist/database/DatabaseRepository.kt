package com.husiev.dynassist.database

import com.husiev.dynassist.components.utils.StartAccountInfo
import com.husiev.dynassist.components.utils.asEntity
import com.husiev.dynassist.database.entity.PlayersEntity
import com.husiev.dynassist.database.entity.asExternalModel
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
	
	suspend fun addPlayer(player: StartAccountInfo) {
		database.playersDao().insert(player.asEntity())
	}
	
	suspend fun deletePlayer(player: StartAccountInfo) {
		database.playersDao().delete(player.asEntity())
	}
}