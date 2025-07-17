package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.husiev.dynassist.database.entity.GlobalRatingSubSet
import com.husiev.dynassist.database.entity.StatisticsEntity
import com.husiev.dynassist.database.entity.StatisticsWithVehicleNames
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao: BaseDao<StatisticsEntity> {
	@Query("SELECT * FROM statistics where account_id = :accountId")
	fun loadStatisticsData(accountId: Int): Flow<List<StatisticsEntity>>
	
	@Query("SELECT battles FROM statistics WHERE account_id = :accountId ORDER BY battles DESC LIMIT 1")
	fun loadLastBattlesCount(accountId: Int): Flow<Int>
	
	@Query("SELECT global_rating, last_battle_time FROM statistics WHERE account_id = :accountId")
	fun loadGlobalRatingData(accountId: Int): Flow<List<GlobalRatingSubSet>>
	
	@Transaction
	@Query("""
		SELECT
            s.*,
            v_xp.name AS max_xp_tank_name,
            v_dmg.name AS max_damage_tank_name,
            v_frags.name AS max_frags_tank_name
        FROM
            statistics AS s
        LEFT JOIN
            vehicle_short_data AS v_xp ON s.max_xp_tank_id = v_xp.tank_id
        LEFT JOIN
            vehicle_short_data AS v_dmg ON s.max_damage_tank_id = v_dmg.tank_id
        LEFT JOIN
            vehicle_short_data AS v_frags ON s.max_frags_tank_id = v_frags.tank_id
        WHERE
            s.account_id = :accountId
		ORDER BY
            s.id DESC
        LIMIT 2
    """)
	fun getStatisticsWithVehicleNames(accountId: Int): Flow<List<StatisticsWithVehicleNames>>
	
}