package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.husiev.dynassist.database.entity.PlayersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao: BaseDao<PlayersEntity> {
	@Query("SELECT * FROM players")
	fun loadPlayersList(): Flow<List<PlayersEntity>>
	
	@Query("SELECT * FROM players WHERE account_id = :accountId")
	fun loadPlayer(accountId: Int): Flow<PlayersEntity>
	
	@Query("UPDATE players SET update_time = :updateTime WHERE account_id = :accountId")
	suspend fun updateTime(updateTime: String, accountId: Int)
	
	@Query("UPDATE players SET clan = :clan, emblem = :emblem WHERE account_id = :accountId")
	suspend fun updateClan(clan: String?, emblem: String?, accountId: Int)
	
	@Query("SELECT * FROM players WHERE notification > 0")
	fun checkedPlayers(): Flow<List<PlayersEntity>>
	
	@Query("UPDATE players SET notification = :notification, notified_battles = :notifiedBattles WHERE account_id = :accountId")
	suspend fun updateNotification(notification: Int, notifiedBattles: Int, accountId: Int)
}