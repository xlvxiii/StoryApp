package com.example.storyapp.ui.story_detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.R
import com.example.storyapp.data.repositories.Result
import com.example.storyapp.databinding.ActivityStoryDetailBinding
import com.google.android.material.snackbar.Snackbar

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityStoryDetailBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(EXTRA_ID) ?: ""
        val factory = ViewModelFactory.getInstance(this)
        val storyDetailViewModel: StoryDetailViewModel by viewModels {
            factory
        }

        storyDetailViewModel.getStoryById(id).observe(this) { story ->
            if (story != null) {
                when (story) {
                    is Result.Loading -> {
                        binding.apply {
                            main.alpha = 0.5f
                            progressCircular.visibility = View.VISIBLE
                        }
                    }
                    is Result.Success -> {
                        binding.main.alpha = 1f
                        binding.progressCircular.visibility = View.GONE
                        binding.tvDetailName.text = story.data.name
                        binding.tvDetailDescription.text = story.data.description
                        Glide.with(applicationContext).load(story.data.photoUrl)
                            .apply(RequestOptions
                                .placeholderOf(R.drawable.outline_image_24)
                                .error(R.drawable.rounded_broken_image_24))
                            .into(binding.ivDetailPhoto)
                    }
                    is Result.Error -> {
                        binding.progressCircular.visibility = View.GONE
                        binding.main.alpha = 1f
                        Snackbar.make(binding.root, story.error, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID: String = "story_id"
    }
}