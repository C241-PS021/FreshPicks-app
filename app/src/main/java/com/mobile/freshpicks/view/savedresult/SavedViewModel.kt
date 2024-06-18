package com.mobile.freshpicks.view.savedresult

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.freshpicks.data.UserRepository
import com.mobile.freshpicks.data.api.response.FruitList
import com.mobile.freshpicks.data.api.response.ScanHistoryItem
import kotlinx.coroutines.launch

class SavedViewModel(private val repository: UserRepository) : ViewModel() {

    private val _savedList = MutableLiveData<List<ScanHistoryItem>?>()
    val savedList: LiveData<List<ScanHistoryItem>?> = _savedList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSavedResult() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val results = repository.getUserHistory().data?.scanHistory
                Log.d("SavedViewModel", "ResultList: $results")
                _savedList.value = results
            } catch (e: Exception) {
                e.printStackTrace()
                _savedList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteResultById(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.deleteHistoryByID(id)
                getSavedResult()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAllResult() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.deleteAllHistory()
                getSavedResult()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}