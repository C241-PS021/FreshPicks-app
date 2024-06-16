package com.mobile.freshpicks.data.api.response

import com.google.gson.annotations.SerializedName

data class GetUserDetailResponse (

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userID")
    val userID: String,

    @field:SerializedName("data")
    val data: DataLogin
)