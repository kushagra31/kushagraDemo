package com.example.model.models

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf()
)
