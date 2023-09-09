package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleStatDao: BaseDao<VehicleStatDataEntity> {
	@Query("SELECT * FROM vehicle_stat where tank_id = :tankId")
	fun loadVehicleStatData(tankId: Int): Flow<List<VehicleStatDataEntity>>
}