package com.gde.university.core.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<typeIntent : UiIntent, typeState : UiState, typeEffect : UiEffect> : ViewModel() {

    private val initialState: typeState by lazy { createInitialState() }
    abstract fun createInitialState(): typeState

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<typeEffect>()
    val effect = _effect.asSharedFlow()

    fun sendIntent(intent: typeIntent) {
        handleIntent(intent)
    }

    protected abstract fun handleIntent(intent: typeIntent)

    protected fun setState(reduce: typeState.() -> typeState) {
        _uiState.update { 
            val newState = it.reduce()
            newState
        }
    }

    protected fun setEffect(inputEffect: () -> typeEffect) {
        val effectValue = inputEffect()
        viewModelScope.launch { _effect.emit(effectValue) }
    }
}
