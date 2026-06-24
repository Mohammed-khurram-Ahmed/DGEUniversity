package com.gde.university.data.mapper

import com.gde.university.data.database.UniversityEntity
import com.gde.university.data.remote.UniversityDto
import org.junit.Assert.assertEquals
import org.junit.Test

class UniversityModelMapperTest {

    @Test
    fun `UniversityDto toEntity converts correctly`() {
        // Given
        val dto = UniversityDto(
            name = "Test Uni",
            country = "Country",
            stateProvince = "State",
            webPages = listOf("page"),
            domains = listOf("domain"),
            alphaTwoCode = "TC"
        )

        // When
        val entity = dto.toEntity()

        // Then
        assertEquals(dto.name, entity.name)
        assertEquals(dto.country, entity.country)
        assertEquals(dto.stateProvince, entity.stateProvince)
        assertEquals(dto.webPages, entity.webPages)
        assertEquals(dto.domains, entity.domains)
        assertEquals(dto.alphaTwoCode, entity.alphaTwoCode)
    }

    @Test
    fun `UniversityDto toEntity converts correctly with null stateProvince`() {
        // Given
        val dto = UniversityDto(
            name = "Test Uni",
            country = "Country",
            stateProvince = null,
            webPages = listOf("page"),
            domains = listOf("domain"),
            alphaTwoCode = "TC"
        )

        // When
        val entity = dto.toEntity()

        // Then
        assertEquals(dto.name, entity.name)
        assertEquals(null, entity.stateProvince)
    }

    @Test
    fun `UniversityEntity toDomain converts correctly`() {
        // Given
        val entity = UniversityEntity(
            name = "Test Uni",
            country = "Country",
            stateProvince = "State",
            webPages = listOf("page"),
            domains = listOf("domain"),
            alphaTwoCode = "TC"
        )

        // When
        val domain = entity.toDomain()

        // Then
        assertEquals(entity.name, domain.name)
        assertEquals(entity.country, domain.country)
        assertEquals(entity.stateProvince, domain.stateProvince)
        assertEquals(entity.webPages, domain.webPages)
        assertEquals(entity.domains, domain.domains)
        assertEquals(entity.alphaTwoCode, domain.alphaTwoCode)
    }

    @Test
    fun `UniversityEntity toDomain converts correctly with null stateProvince`() {
        // Given
        val entity = UniversityEntity(
            name = "Test Uni",
            country = "Country",
            stateProvince = null,
            webPages = listOf("page"),
            domains = listOf("domain"),
            alphaTwoCode = "TC"
        )

        // When
        val domain = entity.toDomain()

        // Then
        assertEquals(entity.name, domain.name)
        assertEquals(null, domain.stateProvince)
    }
}
