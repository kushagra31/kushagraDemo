package com.example.data.repository

import com.example.model.StockDetailResponse
import com.example.network.StocksApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface HoldingsRepository {
    fun getStocks(): Flow<Result<List<StockDetailResponse>>>
}

class HoldingsRepositoryImpl @Inject constructor(
    private val stocksApiService: StocksApiService,
) : HoldingsRepository {
    override fun getStocks() =
        flow {
            emit(Result.success(stocksApiService.getStocks().data.results))
        }.catch { e ->
            emit(Result.failure(e))
        }
}

