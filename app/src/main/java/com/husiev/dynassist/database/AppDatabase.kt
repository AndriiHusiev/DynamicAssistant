package com.husiev.dynassist.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.husiev.dynassist.database.dao.*
import com.husiev.dynassist.database.entity.*

@Database(
	entities = [
		PlayersEntity::class,
		PersonalEntity::class,
		StatisticsEntity::class,
		ClanEntity::class,
		VehicleInfoEntity::class,
		VehicleStatDataEntity::class,
	],
	version = 5,
	autoMigrations = [
		AutoMigration(from = 1, to = 2),
		AutoMigration(from = 2, to = 3),
		AutoMigration(from = 4, to = 5),
	],
	exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
	
	abstract fun playersDao(): PlayersDao
	abstract fun personalDataDao(): PersonalDataDao
	abstract fun statisticsDao(): StatisticsDao
	abstract fun clanDataDao(): ClanDataDao
	abstract fun vehicleInfoDao(): VehicleInfoDao
	abstract fun vehicleStatDao(): VehicleStatDao
	
}