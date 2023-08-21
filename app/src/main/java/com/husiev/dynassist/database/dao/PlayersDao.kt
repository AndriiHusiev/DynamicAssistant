package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.husiev.dynassist.database.entity.PlayersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao: BaseDao<PlayersEntity> {
	@Query("SELECT * FROM players")
	fun loadPlayersList(): Flow<List<PlayersEntity>>
	
	@Query("SELECT * FROM players where account_id = :accountId")
	fun loadPlayer(accountId: String): Flow<PlayersEntity>
	
	@Query("UPDATE players SET update_time = :updateTime WHERE account_id = :accountId")
	fun updateTime(updateTime: String, accountId: Int)
}