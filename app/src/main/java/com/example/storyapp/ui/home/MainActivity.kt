package com.example.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.login.LoginActivity

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

        homeViewModel.getName().observe(this) {
            binding.tvName.text = it
        }

        binding.btnLogout.setOnClickListener {
            homeViewModel.logout()
        }
    }
}