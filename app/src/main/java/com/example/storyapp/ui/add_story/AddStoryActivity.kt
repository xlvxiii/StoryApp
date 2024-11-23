package com.example.storyapp.ui.add_story

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.data.repositories.Result
import com.example.storyapp.data.response.StoryUploadResponse
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.ui.home.MainActivity
import com.example.storyapp.utils.getImageUri
import com.example.storyapp.utils.uriToFile
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var imageUri: Uri

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            binding.imgPreview.setImageURI(imageUri)
        } else {
            Log.d("Photo Picker", "No media selected")
            Snackbar.make(binding.root, "No media selected", Snackbar.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            imageUri.let { uri ->
                Log.d("Image URI", "showImage: $uri")
                binding.imgPreview.setImageURI(uri)
            }
        } else {
            Log.d("Camera intent", "No photo taken")
            Snackbar.make(binding.root, "No photo taken", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initiate viewModel
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AddStoryViewModel::class.java]

        binding.apply {
            btnGallery.setOnClickListener {
                startGallery()
            }

            btnCamera.setOnClickListener {
                startCamera()
            }

            btnUpload.setOnClickListener {
                if (this@AddStoryActivity::imageUri.isInitialized) {
                    uploadStory()
                } else {
                    Snackbar.make(binding.root, "Please select an image first", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        imageUri = getImageUri(this)
        launcherIntentCamera.launch(imageUri)
    }

    private fun uploadStory() {
        imageUri.let { uri ->
            val imageFile = uriToFile(uri, this) //.reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val desc = binding.edAddDescription.text.toString()
            showLoading(true)

            val requestBody = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            lifecycleScope.launch {
                viewModel.addStory(requestBody, multipartBody).observe(this@AddStoryActivity) { result: Result<StoryUploadResponse>? ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                Log.d("AddStoryActivity", "uploadStory: ${result.data.message}")
                                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressCircular.visibility = View.VISIBLE
                main.alpha = 0.5f
            } else {
                progressCircular.visibility = View.GONE
                main.alpha = 1f
            }
        }
    }
}