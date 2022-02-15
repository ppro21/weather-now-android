package com.pprodev.weathernow.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pprodev.weathernow.repository.SharedRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SharedViewModel : ViewModel() {
    private val repository = SharedRepository()

    fun getCurrentWeather() {
        viewModelScope.launch {
            val response = repository.getCurrentWeather()
            Timber.i("WEATHER RESPONSE: [%s]", response)
        }
    }
}