package com.gde.university.feature.listing.mvi

import com.gde.university.core.mvi.UiState
import com.gde.university.domain.model.UniversityModel

data class ListingState(
    val isLoading: Boolean = false,
    val universities: List<UniversityModel> = emptyList(),
    val errorResId: Int? = null
) : UiState