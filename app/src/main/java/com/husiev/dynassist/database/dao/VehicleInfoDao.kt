package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.husiev.dynassist.database.entity.VehicleInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleInfoDao: BaseDao<VehicleInfoEntity> {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrReplace(list: List<VehicleInfoEntity>)
	
	@Query("SELECT * FROM vehicle_short_data")
	fun loadVehicleInfo(): Flow<List<VehicleInfoEntity>>
	
	@Query("SELECT * FROM vehicle_short_data WHERE tank_id IN (:tanks)")
	fun loadExactVehicleInfo(tanks: List<Int>): Flow<List<VehicleInfoEntity>>
	
	@Query("SELECT tank_id FROM vehicle_short_data WHERE tank_id IN (:tanks)")
	fun loadExactVehicleIds(tanks: List<Int>): Flow<List<Int>>
}