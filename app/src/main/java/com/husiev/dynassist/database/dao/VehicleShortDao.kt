package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.husiev.dynassist.database.entity.VehicleShortDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleShortDao: BaseDao<VehicleShortDataEntity> {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplace(list: List<VehicleShortDataEntity>)
	
	@Query("SELECT * FROM vehicle_short_data where account_id = :accountId")
	fun loadVehicleShortData(accountId: Int): Flow<List<VehicleShortDataEntity>>
}