package com.gde.university.data.datasource.remote

import com.gde.university.data.remote.UniversityApiService
import com.gde.university.data.remote.UniversityDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UniversityRemoteDataSourceImplTest {

    private lateinit var apiService: UniversityApiService
    private lateinit var dataSource: UniversityRemoteDataSourceImpl

    @Before
    fun setUp() {
        apiService = mockk()
        dataSource = UniversityRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getUniversities should return list from api service`() = runTest {
        // Given
        val universityDtos = listOf(
            UniversityDto(
                name = "Test University",
                country = "Test Country",
                stateProvince = null,
                webPages = listOf("www.test.edu"),
                domains = listOf("test.edu"),
                alphaTwoCode = "TC"
            )
        )
        coEvery { apiService.getUniversities() } returns universityDtos

        // When
        val result = dataSource.getUniversities()

        // Then
        assertEquals(universityDtos, result)
        coVerify(exactly = 1) { apiService.getUniversities() }
    }
}
