package com.example.model.models

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("name") val name: String,
    @SerializedName("job") val job: String
)