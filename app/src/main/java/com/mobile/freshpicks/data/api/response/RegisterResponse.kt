package com.mobile.freshpicks.data.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userID")
    val userID: String,

    @field:SerializedName("data")
    val data: DataRegister
)

data class DataRegister (

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("dateOfRegistration")
    val dateOfRegistration: String
)

