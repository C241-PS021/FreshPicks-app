package com.mobile.freshpicks.di

import android.content.Context
import com.mobile.freshpicks.data.UserRepository
import com.mobile.freshpicks.data.api.retrofit.ApiConfig
import com.mobile.freshpicks.data.pref.UserPreference
import com.mobile.freshpicks.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}