package com.gde.university.core.mvi

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // Mock UI components for testing
    sealed interface TestIntent : UiIntent {
        data object Increment : TestIntent
    }

    data class TestState(val count: Int) : UiState

    sealed interface TestEffect : UiEffect {
        data object ShowToast : TestEffect
    }

    class TestViewModel : BaseViewModel<TestIntent, TestState, TestEffect>() {
        override fun createInitialState(): TestState = TestState(0)

        override fun handleIntent(intent: TestIntent) {
            when (intent) {
                is TestIntent.Increment -> {
                    setState { copy(count = count + 1) }
                    setEffect { TestEffect.ShowToast }
                }
            }
        }
    }

    private lateinit var viewModel: TestViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TestViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        assertEquals(TestState(0), viewModel.uiState.value)
    }

    @Test
    fun `handleIntent updates state correctly`() = runTest {
        viewModel.uiState.test {
            assertEquals(TestState(0), awaitItem())
            viewModel.sendIntent(TestIntent.Increment)
            assertEquals(TestState(1), awaitItem())
        }
    }

    @Test
    fun `setEffect emits effect correctly`() = runTest {
        viewModel.effect.test {
            viewModel.sendIntent(TestIntent.Increment)
            assertEquals(TestEffect.ShowToast, awaitItem())
        }
    }
}
