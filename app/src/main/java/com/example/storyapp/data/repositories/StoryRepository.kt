package com.example.storyapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.ErrorResponse
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.data.response.Story
import com.example.storyapp.data.response.StoryUploadResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

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

    fun getStoryById(id: String): LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryById(id)
            val story = response.story
            emit(Result.Success(story))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
            Log.d("StoryRepository", "getStoryById: ${e.message.toString()}")
        }
    }

    suspend fun addStory(requestBody: RequestBody, multipartBody: MultipartBody.Part): LiveData<Result<StoryUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(multipartBody, requestBody)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryUploadResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("StoryRepository", "addStory: ${e.message.toString()} $errorMessage")
            emit(Result.Error(errorMessage))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}