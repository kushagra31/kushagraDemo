package com.example.kushagraDemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.kushagraDemo.holdings.FetchStatus
import com.example.kushagraDemo.holdings.HoldingsViewModel
import com.example.data.repository.HoldingsRepository
import com.example.model.StockDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HoldingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockRepository: HoldingsRepository
    private lateinit var viewModel: HoldingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mock()
        val mockResponse = Result.success(
            listOf(
                StockDetailResponse("ICICI", 10, 150.0f, 140.0f, 145.0f),
                StockDetailResponse("HDFC", 5, 2500.0f, 2400.0f, 2450.0f)
            )
        )
        whenever(mockRepository.getStocks()).thenReturn(flow { emit(mockResponse) })
        viewModel = HoldingsViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher
    }

    @Test
    fun `fetchUserStockDetails success updates state correctly`() = runTest {
        // Given
        val mockResponse = Result.success(
            listOf(
                StockDetailResponse("ICICI", 10, 150.0f, 140.0f, 145.0f),
                StockDetailResponse("HDFC", 5, 2500.0f, 2400.0f, 2450.0f)
            )
        )

        whenever(mockRepository.getStocks()).thenReturn(flow { emit(mockResponse) })

        // When
        viewModel.fetchUserStockDetails()
        advanceUntilIdle()

        // Then
        val state = viewModel.viewState
        assertEquals(FetchStatus.Fetched, state.fetchStatus)
        assertEquals(2, state.stocksDetail.size)
        assertNull(state.errorMessage)

        // Verify calculations (example for ICICI)
        val stockDetails = state.stocksDetail.find { it.symbol == "ICICI" }
        assertNotNull(stockDetails)
        assertEquals(1500.0f, stockDetails!!.currentValue) // 10 * 150.0f
        assertEquals(1450.0f, stockDetails.investmentValue) // 10 * 145.0f
        assertEquals(50.0f, stockDetails.pnlValue)

        // Verify total calculations
        val expectedTotalCurrent = (10 * 150.0f) + (5 * 2500.0f)
        val expectedTotalInvestment = (10 * 145.0f) + (5 * 2450.0f)
        val todayPnlValue = (-10f * 10) + (-100f * 5)
        assertEquals(expectedTotalCurrent, state.totalCurrentValue)
        assertEquals(expectedTotalInvestment, state.totalInvestment)
        assertEquals(state.totalCurrentValue - state.totalInvestment, state.totalPnlValue)
        assertEquals(todayPnlValue, state.todayPnlValue)
    }
//
//    @Test
//    fun `fetchUserStockDetails network error updates state with IO error`() = runTest {
//        // Given
//        val errorMessage = "Network error. Please check your connection."
//        whenever(mockRepository.getStocks()).thenReturn(flow {
//            Result.failure<IOException>(
//                IOException()
//            )
//        })
//        // When
//        viewModel.fetchUserStockDetails()
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Then
//        val state = viewModel.viewStates.value
//        assertEquals(FetchStatus.NotFetched, state.fetchStatus)
//        assertEquals(errorMessage, state.errorMessage)
//    }
//
//    @Test
//    fun `fetchUserStockDetails other error updates state with generic error`() = runTest {
//        // Given
//        val genericErrorMessage = "An unexpected error occurred."
//        whenever(mockRepository.getStocks()).thenReturn(flow {
//            Result.failure<RuntimeException>(
//                RuntimeException()
//            )
//        })
//
//        // When
//        viewModel.fetchUserStockDetails()
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Then
//        val state = viewModel.viewStates.value
//        assertEquals(FetchStatus.NotFetched, state.fetchStatus)
//        assertEquals(genericErrorMessage, state.errorMessage)
//    }

    @Test
    fun `fetchUserStockDetails empty response updates state correctly`() = runTest {
        // Given
        val mockEmptyResponse = Result.success(emptyList<StockDetailResponse>())
        whenever(mockRepository.getStocks()).thenReturn(flow { emit(mockEmptyResponse) })

        // When
        viewModel.fetchUserStockDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.viewState
        assertEquals(FetchStatus.Fetched, state.fetchStatus)
    }
}