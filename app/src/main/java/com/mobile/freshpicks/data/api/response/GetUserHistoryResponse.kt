package com.mobile.freshpicks.data.api.response

import com.google.gson.annotations.SerializedName

data class GetUserHistoryResponse (

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userID")
    val userID: String,

    @field:SerializedName("scanHistory")
    val scanHistory: List<ScanHistoryItem>
)

data class ScanHistoryItem (

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("scannedImageURL")
    val scannedImageURL: String,

    @field:SerializedName("fruitName")
    val fruitName: String,

    @field:SerializedName("scanResult")
    val scanResult: String
)