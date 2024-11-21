package com.example.storyapp.data.repositories

import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.local.preferences.SessionPreferences

class StoryRepository private constructor(private val apiService: ApiService) {

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, pref: SessionPreferences): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}