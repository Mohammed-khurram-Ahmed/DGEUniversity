package com.gde.university.data.datasource.remote

import com.gde.university.data.remote.UniversityApiService
import com.gde.university.data.remote.UniversityDto
import javax.inject.Inject

interface UniversityRemoteDataSource {
    suspend fun getUniversities(): List<UniversityDto>
}

class UniversityRemoteDataSourceImpl @Inject constructor(
    private val apiService: UniversityApiService
) : UniversityRemoteDataSource {
    override suspend fun getUniversities(): List<UniversityDto> {
        return apiService.getUniversities()
    }
}
