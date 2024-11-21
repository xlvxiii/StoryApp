package com.example.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.local.preferences.SessionPreferences
import com.example.storyapp.data.repositories.StoryRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val sessionPreferences: SessionPreferences
) : ViewModel() {
    fun getStories() = storyRepository.getAllStories()

    fun getSession(): LiveData<String?> {
        return sessionPreferences.getSessionToken().asLiveData()
    }

    fun getName(): LiveData<String?> {
        return sessionPreferences.getName().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            sessionPreferences.clearUserId()
            sessionPreferences.clearName()
            sessionPreferences.clearSessionToken()
        }
    }
}