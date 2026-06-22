package com.gde.university.domain.model

sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val exception: Throwable, val message: String? = null) : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()
}
