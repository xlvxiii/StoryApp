package com.example.storyapp.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.local.preferences.SessionPreferences
import com.example.storyapp.data.repositories.StoryRepository
import com.example.storyapp.di.Injection

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val sessionPreferences: SessionPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository, sessionPreferences) as T
        }

        throw IllegalArgumentException("Unknown model class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this)
        {
            instance ?: ViewModelFactory(
                Injection.provideStoryRepository(context),
                Injection.provideSessionPreferences(context)
            )
        }

        fun clear() {
            instance = null
        }
    }
}