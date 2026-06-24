package com.gde.university.domain.usecase

import app.cash.turbine.test
import com.gde.university.domain.model.DataResult
import com.gde.university.domain.model.UniversityModel
import com.gde.university.domain.repository.UniversityRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetUniversitiesUseCaseTest {

    private lateinit var repository: UniversityRepository
    private lateinit var getUniversitiesUseCase: GetUniversitiesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getUniversitiesUseCase = GetUniversitiesUseCase(repository)
    }

    @Test
    fun `invoke should return flow of DataResult from repository`() = runTest {
        // Given
        val universities = listOf(
            UniversityModel("Uni 1", "Country", null, emptyList(), emptyList(), "C1")
        )
        val expectedResult = DataResult.Success(universities)
        every { repository.getUniversities(any()) } returns flowOf(expectedResult)

        // When
        getUniversitiesUseCase(forceRefresh = false).test {
            // Then
            assertEquals(expectedResult, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `invoke with forceRefresh true should call repository with forceRefresh true`() = runTest {
        // Given
        val expectedResult = DataResult.Loading
        every { repository.getUniversities(true) } returns flowOf(expectedResult)

        // When
        getUniversitiesUseCase(forceRefresh = true).test {
            // Then
            assertEquals(expectedResult, awaitItem())
            awaitComplete()
        }
    }
}
