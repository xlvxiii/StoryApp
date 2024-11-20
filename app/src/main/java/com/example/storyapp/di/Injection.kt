package com.example.storyapp.di

import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.repositories.UserRepository

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}