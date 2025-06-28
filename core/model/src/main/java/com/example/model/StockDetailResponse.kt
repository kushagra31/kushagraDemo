package com.example.model

import com.google.gson.annotations.SerializedName

data class StockDetailResponse (
    @SerializedName("symbol") val symbol: String? = null,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("ltp") val ltp: Float,
    @SerializedName("close") val close: Float,
    @SerializedName("avgPrice") val avgPrice: Float
)