package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.husiev.dynassist.database.entity.GlobalRatingSubSet
import com.husiev.dynassist.database.entity.StatisticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao: BaseDao<StatisticsEntity> {
	@Query("SELECT * FROM statistics where account_id = :accountId")
	fun loadStatisticsData(accountId: Int): Flow<List<StatisticsEntity>>
	
	@Query("SELECT battles FROM statistics WHERE account_id = :accountId ORDER BY battles DESC LIMIT 1")
	fun loadLastBattlesCount(accountId: Int): Flow<Int>
	
	@Query("SELECT global_rating, last_battle_time FROM statistics WHERE account_id = :accountId")
	fun loadGlobalRatingData(accountId: Int): Flow<List<GlobalRatingSubSet>>
}