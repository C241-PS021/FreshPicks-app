package com.mobile.freshpicks.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.freshpicks.data.UserRepository
import com.mobile.freshpicks.data.api.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(username: String, email: String, password: String, callback: (RegisterResponse) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.register(username, email, password)
                withContext(Dispatchers.Main) {
                    callback(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                     callback(RegisterResponse(error = true, message = e.localizedMessage))
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

}