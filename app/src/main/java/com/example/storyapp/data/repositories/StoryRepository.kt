package com.example.storyapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.local.preferences.SessionPreferences
import com.example.storyapp.data.response.ListStoryItem

class StoryRepository private constructor(private val apiService: ApiService) {

    fun getAllStories(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories()
            val stories = response.listStory
            emit(Result.Success(stories))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.d("StoryRepository", "getAllStories: ${e.message.toString()}")
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, pref: SessionPreferences): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}