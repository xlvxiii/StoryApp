package com.example.storyapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.example.storyapp.data.repositories.Result

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        val registerViewModel: RegisterViewModel by viewModels {
            factory
        }

        binding.apply {
            btnLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnSignUp.setOnClickListener {
                if (inputName.text.toString().isNotEmpty() && inputName.error == null
                    && inputEmail.text.toString().isNotEmpty() && inputEmail.error == null
                    && inputPassword.text.toString().isNotEmpty() && inputPassword.error == null)
                {
                    val name: String = inputName.text.toString()
                    val email: String = inputEmail.text.toString()
                    val password: String = inputPassword.text.toString()

                    registerViewModel.register(name, email, password).observe(this@RegisterActivity) {
                        response ->
                        if (response != null) {
                            when (response) {
                                is Result.Loading -> {
                                    progressCircular.visibility = View.VISIBLE
                                    main.alpha = 0.5f
                                }
                                is Result.Success -> {
                                    main.alpha = 1f
                                    progressCircular.visibility = View.GONE
                                    showSuccessDialog()
                                    Snackbar.make(root, response.data, Snackbar.LENGTH_SHORT).show()
                                }
                                is Result.Error -> {
                                    main.alpha = 1f
                                    progressCircular.visibility = View.GONE
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
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
            .setMessage("Your account has been successfully created.")

        builder.setPositiveButton("Continue") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        builder.create().show()
    }
}