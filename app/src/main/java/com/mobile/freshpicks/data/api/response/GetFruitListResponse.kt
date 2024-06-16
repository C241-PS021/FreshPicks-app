package com.mobile.freshpicks.data.api.response

import com.google.gson.annotations.SerializedName

data class GetFruitListResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("fruitList")
    val fruitList: List<FruitList>? = emptyList()
)

data class FruitList(
    @field:SerializedName("Name")
    val name: String,

    @field:SerializedName("Description")
    val description: String,

    @field:SerializedName("fruitImageURL")
    val fruitImageURL: String
)
