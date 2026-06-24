package com.gde.university.feature.listing.mvi

import androidx.lifecycle.viewModelScope
import com.gde.university.core.mvi.BaseViewModel
import com.gde.university.domain.model.DataResult
import com.gde.university.domain.model.UniversityModel
import com.gde.university.domain.usecase.GetUniversitiesUseCase
import com.gde.university.feature.listing.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val getUniversitiesUseCase: GetUniversitiesUseCase,
) : BaseViewModel<ListingIntent, ListingState, ListingEffect>() {

    init {
        loadData()
    }

    override fun createInitialState(): ListingState = ListingState()

    override fun handleIntent(intent: ListingIntent) {
        when (intent) {
            is ListingIntent.ForceRefreshData -> loadData(forceRefresh = true)
            is ListingIntent.OnItemClicked -> {
                setEffect { ListingEffect.NavigateToDetails(intent.universityModel) }
            }
        }
    }

    private var lastResult: DataResult<List<UniversityModel>>? = null

    private fun loadData(forceRefresh: Boolean = false) {
        getUniversitiesUseCase(forceRefresh)
            .flowOn(Dispatchers.IO)
            .onEach { result ->
                // here we are  Avoiding processing the same result twice if it's identical
                if (result == lastResult) return@onEach
                lastResult = result

                when (result) {
                    is DataResult.Loading -> {
                        setState { copy(isLoading = true, errorResId = null) }
                    }
                    is DataResult.Success -> {
                        setState { copy(isLoading = false, universities = result.data, errorResId = null) }
                    }
                    is DataResult.Error -> {
                        val errorResId = handleException(result.exception)
                        if (uiState.value.universities.isEmpty()) {
                            setState { copy(isLoading = false, errorResId = errorResId) }
                        } else {
                            setState { copy(isLoading = false) }
                            setEffect { ListingEffect.ShowError(errorResId) }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleException(e: Throwable): Int {
        return if (e is java.net.UnknownHostException || e is java.io.IOException) {
            R.string.error_no_internet
        } else {
            R.string.error_general
        }
    }
}
