package com.example.storyapp.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.local.preferences.SessionPreferences
import com.example.storyapp.data.repositories.UserRepository
import com.example.storyapp.di.Injection

class ViewModelFactory private constructor(private val userRepository: UserRepository, private val sessionPreferences: SessionPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository, sessionPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUserRepository(), Injection.provideSessionPreferences(context))
            }.also { instance = it }
    }
}