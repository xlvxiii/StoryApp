package com.example.storyapp.ui.map

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repositories.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}