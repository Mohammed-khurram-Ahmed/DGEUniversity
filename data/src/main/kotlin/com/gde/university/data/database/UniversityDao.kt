package com.gde.university.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UniversityDao {
    @Query("SELECT * FROM universities")
    fun getUniversities(): Flow<List<UniversityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversities(universities: List<UniversityEntity>)

    @Query("DELETE FROM universities")
    suspend fun clearUniversities()
}
