package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleStatDao: BaseDao<VehicleStatDataEntity> {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertAll(list: List<VehicleStatDataEntity>)
	
	@Query("SELECT * FROM vehicle_stat where account_id = :accountId")
	fun loadAllVehiclesStatData(accountId: Int): Flow<List<VehicleStatDataEntity>>
}