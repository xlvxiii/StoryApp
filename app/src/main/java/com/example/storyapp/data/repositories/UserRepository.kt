package com.example.storyapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.api.ApiService

class UserRepository private constructor(private val apiService: ApiService) {
    fun register(name: String, email: String, password: String) : LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            // Handle the response
            val response = apiService.register(name, email, password)
            val message = response.message
            emit(Result.Success(message))
        } catch (e: Exception) {
            // Handle the exception
            Log.d("UserRepository", "register: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}