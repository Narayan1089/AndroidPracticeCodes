package com.example.practisetest.mvvmPractise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.practisetest.R
import com.example.practisetest.databinding.ActivityMvvmTestBinding

class MvvmTest : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMvvmTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityMvvmTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.greetingMessage.observe(this, Observer { message ->
            binding.tvGreeting.text = message
        })

        mainViewModel.greetingMessage.observe(this) {
            binding.tvGreeting.text = it
        }

        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString()
            mainViewModel.setName(name)
        }
    }
}