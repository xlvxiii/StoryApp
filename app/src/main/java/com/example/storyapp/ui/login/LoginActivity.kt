package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyapp.ui.home.MainActivity
import com.example.storyapp.R
import com.example.storyapp.data.repositories.Result
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initiate viewmodel
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this.application)
        val loginViewModel: LoginViewModel by viewModels {
            factory
        }

        binding.apply {
            btnSignUp.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnLogin.setOnClickListener {
                if (edLoginEmail.text.toString().isNotEmpty() && edLoginEmail.error == null
                    && edLoginPassword.text.toString().isNotEmpty() && edLoginPassword.error == null)
                {
                    val email: String = edLoginEmail.text.toString()
                    val password: String = edLoginPassword.text.toString()

                    loginViewModel.login(email, password).observe(this@LoginActivity) {
                        response ->

                        if (response != null) {
                            when (response) {
                                is Result.Loading -> {
                                    circularProgress.visibility = View.VISIBLE
                                    main.alpha = 0.5f
                                }
                                is Result.Success -> {
                                    loginViewModel.saveSession(response.data.token, response.data.userId, response.data.name)
                                    main.alpha = 1f
                                    circularProgress.visibility = View.GONE

                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                is Result.Error -> {
                                    main.alpha = 1f
                                    circularProgress.visibility = View.GONE
                                    Snackbar.make(root, response.error, Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    val snackbar = Snackbar.make(root, R.string.invalid_form, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }

        playAnimation()
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 0f, 1f).setDuration(200)
        val edLoginEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 0f, 1f).setDuration(200)
        val edLoginPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 0f, 1f).setDuration(200)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 0f, 1f).setDuration(200)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 0f, 1f).setDuration(200)

        val together = AnimatorSet().apply {
            playTogether(btnLogin, btnSignup)
        }
        AnimatorSet().apply {
            playSequentially(title, edLoginEmail, edLoginPassword, together)
            start()
        }
    }
}