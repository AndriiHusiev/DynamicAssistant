package com.husiev.dynassist.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
	
	@Provides
	@Singleton
	fun providesAppDatabase(
		@ApplicationContext context: Context,
	): AppDatabase = Room.databaseBuilder(
		context,
		AppDatabase::class.java,
		"app_database",
	)
		.fallbackToDestructiveMigration()
		.addMigrations(MIGRATION_3_4)
		.build()
	
	private val MIGRATION_3_4 = object : Migration(3, 4) {
		override fun migrate(database: SupportSQLiteDatabase) {
			
			// Delete extra columns from 'vehicle_stat'
			database.execSQL("DROP TABLE vehicle_stat")
			database.execSQL("CREATE TABLE vehicle_stat (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
					"account_id INTEGER NOT NULL, tank_id INTEGER NOT NULL, last_battle_time INTEGER, " +
					"mark_of_mastery INTEGER NOT NULL, battles INTEGER NOT NULL, wins INTEGER NOT NULL, " +
					"FOREIGN KEY(`account_id`) REFERENCES `players`(`account_id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
			database.execSQL("CREATE UNIQUE INDEX index_vehicle_stat_account_id_tank_id_battles " +
					"ON vehicle_stat (account_id, tank_id, battles)")
			
			// Copy statistic data from 'vehicle_short_data' to 'vehicle_stat'
			database.execSQL("INSERT INTO vehicle_stat (account_id, tank_id, last_battle_time, " +
					"mark_of_mastery, battles, wins) SELECT account_id, tank_id, lastBattleTime, " +
					"mark_of_mastery, battles, wins FROM vehicle_short_data")
			
			// Delete statistic data from 'vehicle_short_data' and their columns
			database.execSQL("ALTER TABLE vehicle_short_data RENAME TO old_table")
			database.execSQL("CREATE TABLE vehicle_short_data (tank_id INTEGER NOT NULL PRIMARY KEY, " +
					"url_small_icon TEXT, url_big_icon TEXT, price_gold INTEGER, price_credit INTEGER, " +
					"is_wheeled INTEGER, is_premium INTEGER, is_gift INTEGER, name TEXT, type TEXT, " +
					"description TEXT, nation TEXT, tier INTEGER)")
			database.execSQL("INSERT OR IGNORE INTO vehicle_short_data SELECT tank_id, url_small_icon, " +
					"url_big_icon, price_gold, price_credit, is_wheeled, is_premium, is_gift, name, " +
					"type, description, nation, tier FROM old_table")
			database.execSQL("DROP TABLE old_table")
			
		}
	}
}