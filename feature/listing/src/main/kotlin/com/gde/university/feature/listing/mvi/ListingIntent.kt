package com.gde.university.feature.listing.mvi

import com.gde.university.core.mvi.UiIntent
import com.gde.university.domain.model.UniversityModel

sealed class ListingIntent : UiIntent {


    object ForceRefreshData : ListingIntent()
    data class OnItemClicked(val universityModel: UniversityModel) : ListingIntent()
}