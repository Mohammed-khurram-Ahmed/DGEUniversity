package com.gde.university.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UniversityModel(
    val name: String,
    val country: String,
    val stateProvince: String?,
    val webPages: List<String>,
    val domains: List<String>,
    val alphaTwoCode: String
) : Parcelable
