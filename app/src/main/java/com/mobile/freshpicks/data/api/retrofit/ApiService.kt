package com.mobile.freshpicks.data.api.retrofit

import com.mobile.freshpicks.data.api.response.DeleteAllHistoryResponse
import com.mobile.freshpicks.data.api.response.DeleteHistoryByScanIDResponse
import com.mobile.freshpicks.data.api.response.GetUserDetailResponse
import com.mobile.freshpicks.data.api.response.GetUserHistoryResponse
import com.mobile.freshpicks.data.api.response.LoginResponse
import com.mobile.freshpicks.data.api.response.RegisterResponse
import com.mobile.freshpicks.data.api.response.UploadResultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("/register")
    suspend fun register (
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("/login")
    suspend fun login (
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("/user")
    suspend fun getUserDetail (
        @Header("Authorization") token: String,
    ): GetUserDetailResponse

    @Multipart
    @POST("/user/scan-result-history")
    suspend fun uploadScanResult (
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("fruitName") fruitName: RequestBody,
        @Part("scanResult") scanResult: RequestBody
    ) : UploadResultResponse

    @GET("/user/scan-result-history")
    suspend fun getUserHistory (
        @Header("Authorization") token: String,
        @Query("fruitName") fruitName: String,
        @Query("scanResult") scanResult: String
    ): GetUserHistoryResponse

    @DELETE("/user/scan-result-history/{scanID}")
    suspend fun deleteHistoryByID (
        @Header("Authorization") token: String,
        @Path("scanID") scanID: String
    ): DeleteHistoryByScanIDResponse

    @DELETE("/user/scan-result-history")
    suspend fun deleteAllHistory (
        @Header("Authorization") token: String
    ): DeleteAllHistoryResponse
}