package com.mobile.freshpicks.data.api.response

import com.google.gson.annotations.SerializedName

data class DeleteHistoryByScanIDResponse (

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userID")
    val userID: String,

    @field:SerializedName("scanID")
    val scanID: String,
)