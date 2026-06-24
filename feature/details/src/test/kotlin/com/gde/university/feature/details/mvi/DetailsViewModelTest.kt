package com.gde.university.feature.details.mvi

import app.cash.turbine.test
import com.gde.university.domain.model.UniversityModel
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
class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailsViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        assertEquals(DetailsState(), viewModel.uiState.value)
    }

    @Test
    fun `LoadDetails intent updates state with university`() = runTest {
        val universityModel = UniversityModel("Uni 1", "Country", null, emptyList(), emptyList(), "C1")
        
        viewModel.uiState.test {
            assertEquals(DetailsState(), awaitItem())
            viewModel.sendIntent(DetailsIntent.LoadDetails(universityModel))
            assertEquals(DetailsState(universityModel), awaitItem())
        }
    }

    @Test
    fun `RefreshRequested intent sets CloseWithRefresh effect`() = runTest {
        viewModel.effect.test {
            viewModel.sendIntent(DetailsIntent.RefreshRequested)
            assertEquals(DetailsEffect.CloseWithRefresh, awaitItem())
        }
    }
}
