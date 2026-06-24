package com.gde.university.feature.listing.mvi

import app.cash.turbine.test
import com.gde.university.domain.model.DataResult
import com.gde.university.domain.model.UniversityModel
import com.gde.university.domain.usecase.GetUniversitiesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ListingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getUniversitiesUseCase: GetUniversitiesUseCase
    private lateinit var viewModel: ListingViewModel
    
    private val resultFlow = MutableStateFlow<DataResult<List<UniversityModel>>>(DataResult.Loading)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getUniversitiesUseCase = mockk()
        
        every { getUniversitiesUseCase(any()) } returns resultFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = ListingViewModel(getUniversitiesUseCase)
    }

    @Test
    fun `initial state is correct and triggers loading`() = runTest {
        createViewModel()
        // Wait for coroutines in init to run
        advanceUntilIdle()
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `Success updates state with universities`() = runTest {
        createViewModel()
        
        viewModel.uiState.test {
            // First item from uiState.value (initial)
            assertEquals(false, awaitItem().isLoading)
            
            // Wait for init block coroutine to process the initial Loading result
            advanceUntilIdle()
            assertEquals(true, awaitItem().isLoading)
            
            val universities = listOf(
                UniversityModel("Uni 1", "Country", null, emptyList(), emptyList(), "C1")
            )
            resultFlow.value = DataResult.Success(universities)
            val successState = awaitItem()
            assertEquals(universities, successState.universities)
            assertEquals(false, successState.isLoading)
        }
    }

    @Test
    fun `Error updates state with errorResId`() = runTest {
        createViewModel()
        
        viewModel.uiState.test {
            assertEquals(false, awaitItem().isLoading) // Initial
            advanceUntilIdle()
            assertEquals(true, awaitItem().isLoading) // Loading from resultFlow
            
            resultFlow.value = DataResult.Error(IOException())
            val errorState = awaitItem()
            assertEquals(com.gde.university.feature.listing.R.string.error_no_internet, errorState.errorResId)
            assertEquals(false, errorState.isLoading)
        }
    }

    @Test
    fun `ForceRefreshData intent triggers new use case call`() = runTest {
        createViewModel()
        advanceUntilIdle()
        
        viewModel.sendIntent(ListingIntent.ForceRefreshData)
        advanceUntilIdle()

        // Verify that the use case was called with forceRefresh = true
        verify { getUniversitiesUseCase(true) }
    }

    @Test
    fun `OnItemClicked intent sets NavigateToDetails effect`() = runTest {
        createViewModel()
        val universityModel = UniversityModel("Uni 1", "Country", null, emptyList(), emptyList(), "C1")
        
        viewModel.effect.test {
            viewModel.sendIntent(ListingIntent.OnItemClicked(universityModel))
            val effect = awaitItem()
            assert(effect is ListingEffect.NavigateToDetails)
            assertEquals(universityModel, (effect as ListingEffect.NavigateToDetails).universityModel)
        }
    }
}
