package com.example.storyapp.ui.stoy_detail

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repositories.StoryRepository

class StoryDetailViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoryById(id: String) = storyRepository.getStoryById(id)
}