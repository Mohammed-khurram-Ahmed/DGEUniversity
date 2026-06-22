package com.gde.university.data.datasource.local

import com.gde.university.data.database.UniversityDao
import com.gde.university.data.database.UniversityEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UniversityLocalDataSource {
    fun getUniversities(): Flow<List<UniversityEntity>>
    suspend fun syncUniversities(entities: List<UniversityEntity>)
    suspend fun clearAll()
}

class UniversityLocalDataSourceImpl @Inject constructor(
    private val universityDao: UniversityDao
) : UniversityLocalDataSource {
    override fun getUniversities(): Flow<List<UniversityEntity>> {
        return universityDao.getUniversities()
    }

    override suspend fun syncUniversities(entities: List<UniversityEntity>) {
        universityDao.clearUniversities()
        universityDao.insertUniversities(entities)
    }

    override suspend fun clearAll() {
        universityDao.clearUniversities()
    }
}
