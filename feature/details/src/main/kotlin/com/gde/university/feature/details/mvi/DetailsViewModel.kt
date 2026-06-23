package com.gde.university.feature.details.mvi

import com.gde.university.core.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : BaseViewModel<DetailsIntent, DetailsState, DetailsEffect>(

) {

    override fun createInitialState(): DetailsState = DetailsState()

    override fun handleIntent(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadDetails -> {
                setState { copy(universityModel = intent.universityModel) }
            }
            is DetailsIntent.RefreshRequested -> {
                setEffect { DetailsEffect.CloseWithRefresh }
            }
        }
    }
}
