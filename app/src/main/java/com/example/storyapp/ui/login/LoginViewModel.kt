package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.local.preferences.SessionPreferences
import com.example.storyapp.data.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository, private val sessionPreferences: SessionPreferences) : ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)

    fun saveSession(token: String, userId: String, name: String) {
        viewModelScope.launch {
            sessionPreferences.saveSessionToken(token)
            sessionPreferences.saveUserId(userId)
            sessionPreferences.saveName(name)
        }
    }
}