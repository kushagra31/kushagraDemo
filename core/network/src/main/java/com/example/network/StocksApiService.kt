package com.example.network

import com.example.model.StocksResponse
import retrofit2.http.GET

interface StocksApiService {
    @GET(".")
    suspend fun getStocks(): StocksResponse
}
