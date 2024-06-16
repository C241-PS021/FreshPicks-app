package com.mobile.freshpicks.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: DataLogin? = null
)

data class DataLogin (

    @field:SerializedName("userID")
    val userID: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("token")
    val token: String
)