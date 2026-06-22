package com.gde.university.domain.repository

import com.gde.university.domain.model.DataResult
import com.gde.university.domain.model.UniversityModel
import kotlinx.coroutines.flow.Flow

interface UniversityRepository {
    fun getUniversities(forceRefresh: Boolean): Flow<DataResult<List<UniversityModel>>>
}
