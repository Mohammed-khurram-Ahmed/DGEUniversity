package com.gde.university.data.repository

import app.cash.turbine.test
import com.gde.university.data.database.UniversityEntity
import com.gde.university.data.datasource.local.UniversityLocalDataSource
import com.gde.university.data.datasource.remote.UniversityRemoteDataSource
import com.gde.university.data.mapper.toDomain
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

class UniversityRepositoryImplTest {

    private lateinit var remoteDataSource: UniversityRemoteDataSource
    private lateinit var localDataSource: UniversityLocalDataSource
    private lateinit var repository: UniversityRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        repository = UniversityRepositoryImpl(remoteDataSource, localDataSource)
    }

    private val sampleEntity = UniversityEntity(
        name = "Test University",
        country = "Test Country",
        stateProvince = null,
        webPages = listOf("www.test.edu"),
        domains = listOf("test.edu"),
        alphaTwoCode = "TC"
    )

    private val sampleDto = UniversityDto(
        name = "Test University",
        country = "Test Country",
        stateProvince = null,
        webPages = listOf("www.test.edu"),
        domains = listOf("test.edu"),
        alphaTwoCode = "TC"
    )

    @Test
    fun `getUniversities when cache not empty and forceRefresh false should only emit cache`() = runTest {
        // Given
        val entities = listOf(sampleEntity)
        every { localDataSource.getUniversities() } returns flowOf(entities)

        // When & Then
        repository.getUniversities(forceRefresh = false).test {
            assertEquals(DataResult.Loading, awaitItem())
            assertEquals(DataResult.Success(entities.map { it.toDomain() }), awaitItem())
            awaitComplete()
        }
        coVerify(exactly = 0) { remoteDataSource.getUniversities() }
    }

    @Test
    fun `getUniversities when cache empty should fetch from remote and sync`() = runTest {
        // Given
        val emptyList = emptyList<UniversityEntity>()
        val remoteData = listOf(sampleDto)
        val entities = listOf(sampleEntity)

        // First call to getUniversities() returns empty, subsequent call returns entities after sync
        every { localDataSource.getUniversities() } returnsMany listOf(flowOf(emptyList), flowOf(entities))
        coEvery { remoteDataSource.getUniversities() } returns remoteData
        coEvery { localDataSource.syncUniversities(any()) } returns Unit

        // When & Then
        repository.getUniversities(forceRefresh = false).test {
            assertEquals(DataResult.Loading, awaitItem())
            assertEquals(DataResult.Success(entities.map { it.toDomain() }), awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { remoteDataSource.getUniversities() }
        coVerify(exactly = 1) { localDataSource.syncUniversities(any()) }
    }

    @Test
    fun `getUniversities when remote fails and cache is empty should emit Error`() = runTest {
        // Given
        val exception = Exception("Network error")
        every { localDataSource.getUniversities() } returns flowOf(emptyList())
        coEvery { remoteDataSource.getUniversities() } throws exception

        // When & Then
        repository.getUniversities(forceRefresh = true).test {
            assertEquals(DataResult.Loading, awaitItem())
            assertEquals(DataResult.Error(exception), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getUniversities when remote fails but cache exists should emit Error then Success`() = runTest {
        // Given
        val exception = Exception("Network error")
        val entities = listOf(sampleEntity)
        
        // Initial check sees non-empty cache, but forceRefresh=true forces remote call
        every { localDataSource.getUniversities() } returns flowOf(entities)
        coEvery { remoteDataSource.getUniversities() } throws exception

        // When & Then
        repository.getUniversities(forceRefresh = true).test {
            assertEquals(DataResult.Loading, awaitItem())
            assertEquals(DataResult.Error(exception), awaitItem())
            assertEquals(DataResult.Success(entities.map { it.toDomain() }), awaitItem())
            awaitComplete()
        }
    }
}
