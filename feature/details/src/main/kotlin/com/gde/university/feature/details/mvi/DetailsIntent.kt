package com.gde.university.feature.details.mvi

import com.gde.university.core.mvi.UiIntent
import com.gde.university.domain.model.UniversityModel

sealed class DetailsIntent : UiIntent {
    data class LoadDetails(val universityModel: UniversityModel) : DetailsIntent()
    object RefreshRequested : DetailsIntent()
}