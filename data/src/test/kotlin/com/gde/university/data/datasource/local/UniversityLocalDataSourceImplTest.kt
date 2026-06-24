package com.gde.university.data.datasource.local

import app.cash.turbine.test
import com.gde.university.data.database.UniversityDao
import com.gde.university.data.database.UniversityEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UniversityLocalDataSourceImplTest {

    private lateinit var universityDao: UniversityDao
    private lateinit var dataSource: UniversityLocalDataSourceImpl

    @Before
    fun setUp() {
        universityDao = mockk()
        dataSource = UniversityLocalDataSourceImpl(universityDao)
    }

    @Test
    fun `getUniversities should return flow from dao`() = runTest {
        // Given
        val universities = listOf(
            UniversityEntity(
                name = "Test University",
                country = "Test Country",
                stateProvince = null,
                webPages = listOf("www.test.edu"),
                domains = listOf("test.edu"),
                alphaTwoCode = "TC"
            )
        )
        every { universityDao.getUniversities() } returns flowOf(universities)

        // When & Then
        dataSource.getUniversities().test {
            assertEquals(universities, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `syncUniversities should clear and then insert new entities`() = runTest {
        // Given
        val universities = listOf(
            UniversityEntity(
                name = "Test University",
                country = "Test Country",
                stateProvince = null,
                webPages = listOf("www.test.edu"),
                domains = listOf("test.edu"),
                alphaTwoCode = "TC"
            )
        )
        coEvery { universityDao.clearUniversities() } returns Unit
        coEvery { universityDao.insertUniversities(any()) } returns Unit

        // When
        dataSource.syncUniversities(universities)

        // Then
        coVerify(exactly = 1) { universityDao.clearUniversities() }
        coVerify(exactly = 1) { universityDao.insertUniversities(universities) }
    }

    @Test
    fun `clearAll should call clearUniversities on dao`() = runTest {
        // Given
        coEvery { universityDao.clearUniversities() } returns Unit

        // When
        dataSource.clearAll()

        // Then
        coVerify(exactly = 1) { universityDao.clearUniversities() }
    }
}
