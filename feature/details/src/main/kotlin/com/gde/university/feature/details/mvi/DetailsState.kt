package com.gde.university.feature.details.mvi

import com.gde.university.core.mvi.UiState
import com.gde.university.domain.model.UniversityModel

data class DetailsState(
    val universityModel: UniversityModel? = null
) : UiState