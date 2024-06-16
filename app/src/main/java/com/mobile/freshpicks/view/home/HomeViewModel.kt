package com.mobile.freshpicks.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.freshpicks.data.UserRepository
import com.mobile.freshpicks.data.api.response.FruitList
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _fruitList = MutableLiveData<List<FruitList>?>()
    val fruitList: LiveData<List<FruitList>?> = _fruitList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getFruitsList(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fruits = repository.getFruitsList().fruitList
                Log.d("HomeViewModel", "FruitsList: $fruits")
                _fruitList.value = fruits
            } catch (e: Exception) {
                e.printStackTrace()
                _fruitList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}