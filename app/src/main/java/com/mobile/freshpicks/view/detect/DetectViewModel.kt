package com.mobile.freshpicks.view.detect

import android.media.Image
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.freshpicks.data.UserRepository
import com.mobile.freshpicks.data.api.response.ScanHistoryItem
import com.mobile.freshpicks.data.api.response.UploadResultResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DetectViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<UploadResultResponse>()
    val uploadResult: LiveData<UploadResultResponse> = _uploadResult

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadScanResult(image: MultipartBody.Part, fruitName: RequestBody, scanResult: RequestBody){
        _isLoading.value = true
        Log.d("Upload Scan", "Uploading with image: $image, fruitName: $fruitName, scanResult: $scanResult")
        viewModelScope.launch {
            try {
                val response = repository.uploadScanResult(image, fruitName, scanResult)
                Log.d("Upload Scan", response.toString())
                _uploadResult.value = response
                _isSuccess.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}