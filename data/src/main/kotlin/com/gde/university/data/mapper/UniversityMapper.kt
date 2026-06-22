package com.gde.university.data.mapper

import com.gde.university.data.database.UniversityEntity
import com.gde.university.data.remote.UniversityDto
import com.gde.university.domain.model.UniversityModel

fun UniversityDto.toEntity(): UniversityEntity {
    return UniversityEntity(
        name = name,
        country = country,
        stateProvince = stateProvince,
        webPages = webPages,
        domains = domains,
        alphaTwoCode = alphaTwoCode
    )
}

fun UniversityEntity.toDomain(): UniversityModel {
    return UniversityModel(
        name = name,
        country = country,
        stateProvince = stateProvince,
        webPages = webPages,
        domains = domains,
        alphaTwoCode = alphaTwoCode
    )
}
