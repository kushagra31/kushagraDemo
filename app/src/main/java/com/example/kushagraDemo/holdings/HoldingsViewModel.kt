package com.example.kushagraDemo.holdings

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.example.kushagraDemo.MviViewModel
import com.example.data.repository.HoldingsRepository
import com.example.model.StockDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okio.IOException
import javax.inject.Inject

data class StocksDetailsState(
    val fetchStatus: FetchStatus = FetchStatus.Fetching,
    val stocksDetail: List<StockDetails>,
    val totalPnlValue: Float = 0f,
    val totalInvestment: Float = 0f,
    val totalCurrentValue: Float = 0f,
    val todayPnlValue: Float = 0f,
    val errorMessage: String? = null
)

sealed class FetchStatus {
    object NotFetched : FetchStatus()
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object Retry : FetchStatus()
}

object StocksEffect
object StocksEvent

@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val repository: HoldingsRepository,
) : MviViewModel<StocksDetailsState, StocksEvent, StocksEffect>() {

    init {
        viewState = StocksDetailsState(
            fetchStatus = FetchStatus.Fetching,
            stocksDetail = mutableStateListOf(),
        )
        fetchUserStockDetails()
    }

    fun fetchUserStockDetails() {
        repository.getStocks()
            .onEach {
                updateStockDetails(it.getOrNull()?: emptyList())
            }.catch { e ->
                val errorMessage = when (e) {
                    is IOException -> "Network error. Please check your connection."
                    else -> e.message?: "No message"
                }
                viewState = viewState.copy(
                    fetchStatus = FetchStatus.NotFetched,
                    errorMessage = errorMessage
                )
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun updateStockDetails(stockDetailResponses: List<StockDetailResponse>) {
        stockDetailResponses.updateStockDetailsState()
    }

    private fun List<StockDetailResponse>.updateStockDetailsState() =
        with(this) {
            val stockDetailsList = mutableListOf<StockDetails>()
            var totalInvestment = 0f
            var totalCurrentValue = 0f
            var todayPnlValue = 0f
            forEach {
                val stockDetails = StockDetails(
                    symbol = it.symbol,
                    ltp = it.ltp,
                    quantity = it.quantity,
                    close = it.close,
                    avgPrice = it.avgPrice,
                    currentValue = (it.ltp * it.quantity),
                    investmentValue = (it.avgPrice * it.quantity),
                )
                totalCurrentValue += (it.ltp * it.quantity)
                totalInvestment += (it.avgPrice * it.quantity)
                todayPnlValue += (it.close- it.ltp) * it.quantity
                stockDetailsList.add(stockDetails)
            }
            viewState = viewState.copy(
                stocksDetail = stockDetailsList,
                fetchStatus = FetchStatus.Fetched,
                totalPnlValue = totalCurrentValue - totalInvestment,
                totalInvestment = totalInvestment,
                totalCurrentValue = totalCurrentValue,
                todayPnlValue = todayPnlValue
            )
        }
}

data class StockDetails(
    val symbol: String? = null,
    val quantity: Int,
    val ltp: Float,
    val close: Float,
    val avgPrice: Float,
    val currentValue: Float,
    val investmentValue: Float,
    val pnlValue: Float = currentValue - investmentValue,
)