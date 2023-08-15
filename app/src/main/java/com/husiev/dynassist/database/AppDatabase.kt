package com.husiev.dynassist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.husiev.dynassist.database.dao.PlayersDao
import com.husiev.dynassist.database.entity.PlayersEntity

@Database(
	entities = [
		PlayersEntity::class,
	],
	version = 1,
)
abstract class AppDatabase : RoomDatabase() {
	
	abstract fun playersDao(): PlayersDao
	
}