package com.mobile.freshpicks.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.mobile.freshpicks.data.api.response.DeleteAllHistoryResponse
import com.mobile.freshpicks.data.api.response.DeleteHistoryByScanIDResponse
import com.mobile.freshpicks.data.api.response.GetFruitListResponse
import com.mobile.freshpicks.data.api.response.GetUserDetailResponse
import com.mobile.freshpicks.data.api.response.GetUserHistoryResponse
import com.mobile.freshpicks.data.api.response.LoginResponse
import com.mobile.freshpicks.data.api.response.RegisterResponse
import com.mobile.freshpicks.data.api.response.UploadResultResponse
import com.mobile.freshpicks.data.api.retrofit.ApiService
import com.mobile.freshpicks.data.pref.UserModel
import com.mobile.freshpicks.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class UserRepository (
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): RegisterResponse {
        return apiService.register(username, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun logout(){
        userPreference.logout()
    }

    suspend fun getUserDetail(token: String): GetUserDetailResponse {
        return apiService.getUserDetail(token)
    }

    suspend fun getFruitsList(): GetFruitListResponse {
        return apiService.getFruitsList()
    }

    suspend fun uploadScanResult(
        image: MultipartBody.Part,
        fruitName: RequestBody,
        scanResult: RequestBody
    ): UploadResultResponse {
        return apiService.uploadScanResult(image, fruitName, scanResult)
    }

    suspend fun getUserHistory(
        fruitName: String,
        scanResult: String
    ): GetUserHistoryResponse {
        return apiService.getUserHistory(fruitName, scanResult)
    }

    suspend fun deleteHistoryByID(scanID: String): DeleteHistoryByScanIDResponse {
        return apiService.deleteHistoryByID(scanID)
    }

    suspend fun deleteAllHistory(): DeleteAllHistoryResponse {
        return apiService.deleteAllHistory()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            UserRepository(apiService, userPreference)
    }
}