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
	
	@Query("SELECT * FROM vehicle_short_data")
	fun loadVehicleShortData(): Flow<List<VehicleShortDataEntity>>
	
	@Query("SELECT * FROM vehicle_short_data WHERE tank_id IN (:tanks)")
	fun loadExactVehicleShortData(tanks: List<Int>): Flow<List<VehicleShortDataEntity>>
	
	@Query("SELECT tank_id FROM vehicle_short_data WHERE tank_id IN (:tanks)")
	fun loadExactVehicleIds(tanks: List<Int>): Flow<List<Int>>
}