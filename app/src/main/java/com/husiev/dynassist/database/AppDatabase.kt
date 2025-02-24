package com.husiev.dynassist.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.husiev.dynassist.database.dao.ClanDataDao
import com.husiev.dynassist.database.dao.PersonalDataDao
import com.husiev.dynassist.database.dao.PlayersDao
import com.husiev.dynassist.database.dao.StatisticsDao
import com.husiev.dynassist.database.dao.VehicleStatDao
import com.husiev.dynassist.database.dao.VehicleShortDao
import com.husiev.dynassist.database.entity.ClanEntity
import com.husiev.dynassist.database.entity.PersonalEntity
import com.husiev.dynassist.database.entity.PlayersEntity
import com.husiev.dynassist.database.entity.StatisticsEntity
import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import com.husiev.dynassist.database.entity.VehicleShortDataEntity

@Database(
	entities = [
		PlayersEntity::class,
		PersonalEntity::class,
		StatisticsEntity::class,
		ClanEntity::class,
		VehicleShortDataEntity::class,
		VehicleStatDataEntity::class,
	],
	version = 4,
	autoMigrations = [
		AutoMigration(from = 1, to = 2),
		AutoMigration(from = 2, to = 3),
	],
	exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
	
	abstract fun playersDao(): PlayersDao
	abstract fun personalDataDao(): PersonalDataDao
	abstract fun statisticsDao(): StatisticsDao
	abstract fun clanDataDao(): ClanDataDao
	abstract fun vehicleShortDao(): VehicleShortDao
	abstract fun vehicleStatDao(): VehicleStatDao
	
}