package com.mobile.freshpicks.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobile.freshpicks.data.UserRepository
import com.mobile.freshpicks.di.Injection
import com.mobile.freshpicks.view.detect.DetectViewModel
import com.mobile.freshpicks.view.home.HomeViewModel
import com.mobile.freshpicks.view.login.LoginViewModel
import com.mobile.freshpicks.view.main.MainViewModel
import com.mobile.freshpicks.view.savedresult.SavedViewModel
import com.mobile.freshpicks.view.signup.SignUpViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetectViewModel::class.java) -> {
                DetectViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SavedViewModel::class.java) -> {
                SavedViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))

    }
}