package com.husiev.dynassist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.husiev.dynassist.database.entity.PersonalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalDataDao: BaseDao<PersonalEntity> {
	@Query("SELECT * FROM personal where account_id = :accountId")
	fun loadPersonalData(accountId: Int): Flow<PersonalEntity>
}