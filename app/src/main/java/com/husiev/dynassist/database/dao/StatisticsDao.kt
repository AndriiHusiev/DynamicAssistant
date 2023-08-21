package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.husiev.dynassist.database.entity.StatisticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao: BaseDao<StatisticsEntity> {
	@Query("SELECT * FROM statistics where account_id = :accountId")
	fun loadStatisticsData(accountId: Int): Flow<List<StatisticsEntity>>
}