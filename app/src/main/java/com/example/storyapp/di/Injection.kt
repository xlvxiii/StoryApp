package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.local.preferences.SessionPreferences
import com.example.storyapp.data.local.preferences.dataStore
import com.example.storyapp.data.repositories.StoryRepository
import com.example.storyapp.data.repositories.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService(null)
        return UserRepository.getInstance(apiService)
    }

    fun provideSessionPreferences(context: Context): SessionPreferences {
        return SessionPreferences.getInstance(context.dataStore)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = SessionPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSessionToken().first() }
        val apiService = ApiConfig.getApiService(user)
        return StoryRepository.getInstance(apiService)
    }
}