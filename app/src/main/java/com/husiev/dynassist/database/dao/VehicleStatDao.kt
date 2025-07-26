package com.husiev.dynassist.database.dao

import androidx.room.*
import com.husiev.dynassist.database.entity.FlatVehicleStatAndInfo
import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleStatDao: BaseDao<VehicleStatDataEntity> {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertAll(list: List<VehicleStatDataEntity>)
	
	@Query("SELECT * FROM vehicle_stat WHERE account_id = :accountId")
	fun loadAllVehiclesStatData(accountId: Int): Flow<List<VehicleStatDataEntity>>
	
	@Transaction
	@Query("""
        SELECT
            info.*,
            stat.id AS stat_id,
            stat.account_id AS stat_account_id,
            stat.tank_id AS stat_tank_id,
            stat.last_battle_time AS stat_last_battle_time,
            stat.mark_of_mastery AS stat_mark_of_mastery,
            stat.battles AS stat_battles,
            stat.wins AS stat_wins
        FROM
            vehicle_short_data AS info
        INNER JOIN
            vehicle_stat AS stat ON info.tank_id = stat.tank_id
        WHERE
            stat.account_id = :accountId
    """)
	fun loadFlatVehiclesWithInfo(accountId: Int): Flow<List<FlatVehicleStatAndInfo>>
	
	@Transaction
	@Query("""
        SELECT
            info.*,
            stat.id AS stat_id,
            stat.account_id AS stat_account_id,
            stat.tank_id AS stat_tank_id,
            stat.last_battle_time AS stat_last_battle_time,
            stat.mark_of_mastery AS stat_mark_of_mastery,
            stat.battles AS stat_battles,
            stat.wins AS stat_wins
        FROM
            vehicle_short_data AS info
        INNER JOIN
            vehicle_stat AS stat ON info.tank_id = stat.tank_id
        WHERE
            stat.account_id = :accountId AND stat.tank_id = :tankId
    """)
	fun loadFlatVehicleWithInfo(accountId: Int, tankId: Int): Flow<List<FlatVehicleStatAndInfo>>
}