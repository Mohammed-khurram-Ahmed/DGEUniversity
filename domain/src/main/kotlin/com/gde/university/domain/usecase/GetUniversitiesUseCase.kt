package com.gde.university.domain.usecase

import com.gde.university.domain.model.DataResult
import com.gde.university.domain.model.UniversityModel
import com.gde.university.domain.repository.UniversityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUniversitiesUseCase @Inject constructor(
    private val repository: UniversityRepository
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<DataResult<List<UniversityModel>>> {
        return repository.getUniversities(forceRefresh)
    }
}
