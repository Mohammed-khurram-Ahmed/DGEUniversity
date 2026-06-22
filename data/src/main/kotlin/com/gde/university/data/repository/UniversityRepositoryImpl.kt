package com.gde.university.data.repository

import com.gde.university.data.datasource.local.UniversityLocalDataSource
import com.gde.university.data.datasource.remote.UniversityRemoteDataSource
import com.gde.university.data.mapper.toDomain
import com.gde.university.data.mapper.toEntity
import com.gde.university.domain.model.DataResult
import com.gde.university.domain.model.UniversityModel
import com.gde.university.domain.repository.UniversityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UniversityRepositoryImpl @Inject constructor(
    private val remoteDataSource: UniversityRemoteDataSource,
    private val localDataSource: UniversityLocalDataSource
) : UniversityRepository {

    override fun getUniversities(forceRefresh: Boolean): Flow<DataResult<List<UniversityModel>>> = flow {
        // Emit Loading state initially
        emit(DataResult.Loading)

        // Get initial cache
        val initialCache = localDataSource.getUniversities().first()
        
        // Decide if we need to fetch from network
        val shouldFetch = forceRefresh || initialCache.isEmpty()

        if (shouldFetch) {
            try {
                val remoteData = remoteDataSource.getUniversities()
                localDataSource.syncUniversities(remoteData.map { it.toEntity() })
            } catch (e: Exception) {
                // If network fails, we still want to show what's in cache if available
                val cacheAfterFailure = localDataSource.getUniversities().first()
                if (cacheAfterFailure.isEmpty()) {
                    emit(DataResult.Error(e))
                    return@flow // Stop here if there is no data at all
                } else {
                    // We emit the error first to notify the UI (e.g. show a Toast)
                    emit(DataResult.Error(e))
                }
            }
        }

        // Always emit the latest local database state as a Success stream
        emitAll(localDataSource.getUniversities().map { entities ->
            DataResult.Success(entities.map { it.toDomain() })
        })
    }
}
