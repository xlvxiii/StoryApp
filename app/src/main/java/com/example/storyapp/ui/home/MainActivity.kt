package com.example.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.data.repositories.Result
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initiate viewModel
//        val viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[MainViewModel::class.java]
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this.application)
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        homeViewModel.getSession().observe(this) {
            if (it == null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        binding.btnLogout.setOnClickListener {
            homeViewModel.logout()
        }

        homeViewModel.getName().observe(this) {
            binding.tvName.text = it
        }

        binding.rvStories.layoutManager = LinearLayoutManager(this)

        homeViewModel.getStories().observe(this) { stories ->
            if (stories != null) {
                when (stories) {
                    is Result.Loading -> {
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                    }
                    is Result.Success -> {
                        setStoriesData(stories.data)
                        binding.shimmerLayout.apply {
                            stopShimmer()
                            visibility = View.GONE
                        }
                    }
                    is Result.Error -> {
                        binding.shimmerLayout.apply {
                            stopShimmer()
                            visibility = View.GONE
                        }
                        Log.e("MainActivity", "Error: ${stories.error}")
                        Snackbar.make(this, binding.root, stories.error, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setStoriesData(stories: List<ListStoryItem>) {
        val adapter = StoryAdapter()
        adapter.submitList(stories)
        binding.rvStories.adapter = adapter

        adapter.setOnItemClickCallback(
            object : StoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListStoryItem) {
                    Snackbar.make(binding.root, data.name ?: "empty", Snackbar.LENGTH_SHORT).show()
//                    showDetailActivity(data.id)
                }
            }
        )
    }

//    private fun showDetailActivity(id: String?) {
//        val intent = Intent(this, DetailActivity::class.java)
//        intent.putExtra(DetailActivity.EXTRA_ID, id)
//        startActivity(intent)
//    }
}