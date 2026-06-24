package com.gde.university.data.repository

import app.cash.turbine.test
import com.gde.university.data.datasource.local.UniversityLocalDataSource
import com.gde.university.data.datasource.remote.UniversityRemoteDataSource
import com.gde.university.data.database.UniversityEntity
import com.gde.university.data.remote.UniversityDto
import com.gde.university.domain.model.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UniversityModelRepositoryImplTest {

    private lateinit var remoteDataSource: UniversityRemoteDataSource
    private lateinit var localDataSource: UniversityLocalDataSource
    private lateinit var repository: UniversityRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        repository = UniversityRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getUniversities should emit items from local data source`() = runTest {
        // Given
        val entities = listOf(
            UniversityEntity(
                name = "Test Uni",
                country = "Test Country",
                stateProvince = null,
                webPages = listOf("test.com"),
                domains = listOf("test.com"),
                alphaTwoCode = "TC"
            )
        )
        every { localDataSource.getUniversities() } returns flowOf(entities)
        coEvery { remoteDataSource.getUniversities() } returns emptyList()
        coEvery { localDataSource.syncUniversities(any()) } returns Unit

        // When & Then
        repository.getUniversities(forceRefresh = false).test {
            assertEquals(DataResult.Loading, awaitItem())
            val successResult = awaitItem() as DataResult.Success
            assertEquals(1, successResult.data.size)
            assertEquals("Test Uni", successResult.data[0].name)
            awaitComplete()
        }
    }

    @Test
    fun `getUniversities with forceRefresh should fetch from remote and sync`() = runTest {
        // Given
        val entities = emptyList<UniversityEntity>()
        val dtos = listOf(
            UniversityDto(
                name = "Remote Uni",
                country = "Test Country",
                stateProvince = null,
                webPages = listOf("remote.com"),
                domains = listOf("remote.com"),
                alphaTwoCode = "TC"
            )
        )
        every { localDataSource.getUniversities() } returns flowOf(entities)
        coEvery { remoteDataSource.getUniversities() } returns dtos
        coEvery { localDataSource.syncUniversities(any()) } returns Unit

        // When
        repository.getUniversities(forceRefresh = true).test {
            awaitItem() // Loading
            awaitItem() // Success (from sync)
            awaitComplete()
        }

        // Then
        coVerify { remoteDataSource.getUniversities() }
        coVerify { localDataSource.syncUniversities(any()) }
    }
}
