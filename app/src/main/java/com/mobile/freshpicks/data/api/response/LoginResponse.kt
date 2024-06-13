package com.mobile.freshpicks.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userID")
    val userID: String,

    @field:SerializedName("data")
    val data: DataLogin,

    @field:SerializedName("token")
    val token: String
)

data class DataLogin (

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String
)