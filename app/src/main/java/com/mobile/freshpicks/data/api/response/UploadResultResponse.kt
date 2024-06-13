package com.mobile.freshpicks.data.api.response

import com.google.gson.annotations.SerializedName

data class UploadResultResponse (

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userID")
    val userID: String,

    @field:SerializedName("scanID")
    val scanID: String,

    @field:SerializedName("data")
    val data: DataUpload
)

data class DataUpload (

    @field:SerializedName("fruitName")
    val fruitName: String,

    @field:SerializedName("scanResult")
    val scanResult: String,

    @field:SerializedName("scannedImageURL")
    val scannedImageURL: String,

    @field:SerializedName("createdAt")
    val createdAt: String
)