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
    private val userPreference: UserPreference,
    private val contentResolver: ContentResolver
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

    suspend fun uploadScanResult(
        token: String,
        imageUri: Uri, // Change parameter to Uri
        fruitName: String,
        scanResult: String
    ): UploadResultResponse {

        // Convert Uri to Bitmap
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri))
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageByteArray = byteArrayOutputStream.toByteArray()

        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageByteArray)
        val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

        val fruitNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), fruitName)
        val scanResultPart = RequestBody.create("text/plain".toMediaTypeOrNull(), scanResult)

        return apiService.uploadScanResult(token, imagePart, fruitNamePart, scanResultPart)
    }

    suspend fun getUserHistory(
        token: String,
        fruitName: String,
        scanResult: String
    ): GetUserHistoryResponse {
        return apiService.getUserHistory(token, fruitName, scanResult)
    }

    suspend fun deleteHistoryByID(token: String, scanID: String): DeleteHistoryByScanIDResponse {
        return apiService.deleteHistoryByID(token, scanID)
    }

    suspend fun deleteAllHistory(token: String): DeleteAllHistoryResponse {
        return apiService.deleteAllHistory(token)
    }
}