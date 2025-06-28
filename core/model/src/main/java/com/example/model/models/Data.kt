package com.example.model.models

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("avatar") var avatarUrl: String? = null
)