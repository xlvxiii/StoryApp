package com.example.storyapp.ui.add_story

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repositories.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    var imageUri: Uri? = null
    suspend fun addStory(requestBody: RequestBody, multipartBody: MultipartBody.Part) =
        storyRepository.addStory(requestBody, multipartBody)
}