package com.example.practisetest.mvvmPractise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _greetingMessage = MutableLiveData<String>()
    val greetingMessage: LiveData<String> get() = _greetingMessage

    fun setName(name: String) {
        _greetingMessage.value = "Hello, $name!"
    }
}