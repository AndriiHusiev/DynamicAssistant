package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.husiev.dynassist.database.entity.ClanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClanDataDao: BaseDao<ClanEntity> {
	@Query("SELECT * FROM clan where account_id = :accountId")
	fun loadClanData(accountId: Int): Flow<ClanEntity>
}