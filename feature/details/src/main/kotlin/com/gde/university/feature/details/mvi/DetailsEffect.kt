package com.gde.university.feature.details.mvi

import com.gde.university.core.mvi.UiEffect

sealed class DetailsEffect : UiEffect {
    object CloseWithRefresh : DetailsEffect()
}
