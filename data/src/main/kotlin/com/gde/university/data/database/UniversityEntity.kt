package com.gde.university.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "universities")
data class UniversityEntity(
    @PrimaryKey val name: String,
    val country: String,
    val stateProvince: String?,
    val webPages: List<String>,
    val domains: List<String>,
    val alphaTwoCode: String
)
