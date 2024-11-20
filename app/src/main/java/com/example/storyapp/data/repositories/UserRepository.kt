package com.example.storyapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.RegisterResponse
import com.google.gson.Gson
import retrofit2.HttpException

class UserRepository private constructor(private val apiService: ApiService) {
    fun register(name: String, email: String, password: String) : LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            // Handle the response
            val response = apiService.register(name, email, password)
            val message = response.message
            emit(Result.Success(message))
        } catch (e: HttpException) {
            // Handle the exception
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("UserRepository", "register: ${e.message.toString()} \nError Message: $errorMessage")
            emit(Result.Error(errorMessage))
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