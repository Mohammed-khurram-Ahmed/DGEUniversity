package com.gde.university.feature.listing.mvi

import com.gde.university.core.mvi.UiEffect
import com.gde.university.domain.model.UniversityModel

sealed class ListingEffect : UiEffect {
    data class NavigateToDetails(val universityModel: UniversityModel) : ListingEffect()
    data class ShowError(val messageResId: Int) : ListingEffect()
}
