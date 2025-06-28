package com.example.model

import com.google.gson.annotations.SerializedName

data class StocksResponse (
    @SerializedName("data") val data: DataResponse
)

data class DataResponse (
    @SerializedName("userHolding") val results: ArrayList<StockDetailResponse>
)

