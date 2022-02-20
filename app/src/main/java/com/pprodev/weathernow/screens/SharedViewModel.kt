package com.pprodev.weathernow.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pprodev.weathernow.model.CurrentWeatherResponseModel
import com.pprodev.weathernow.repository.SharedRepository
import kotlinx.coroutines.*

class SharedViewModel constructor(
    private val repository: SharedRepository = SharedRepository()
) : ViewModel() {
    var job: Job? = null

    // LiveData
    private val _weatherIconMutableLiveData = MutableLiveData<String?>()
    val weatherIconLiveData: LiveData<String?> = _weatherIconMutableLiveData

    private val _cityNameMutableLiveData = MutableLiveData<String?>()
    val cityNameLiveData: LiveData<String?> = _cityNameMutableLiveData

    private val _weatherImgNameMutableLiveData = MutableLiveData<String?>()
    val weatherImgNameLiveData: LiveData<String?> = _weatherImgNameMutableLiveData

    fun getCurrentWeather(cityName: String) {

        job = CoroutineScope(Dispatchers.IO).launch {
            val response: CurrentWeatherResponseModel? = repository.getCurrentWeather(cityName)

            withContext(Dispatchers.Main) {
                _cityNameMutableLiveData.value = "${response?.name}, ${response?.sys?.country}"

                _weatherIconMutableLiveData.value =
                    "https://openweathermap.org/img/wn/${response?.weather?.get(0)?.icon}@4x.png"

                _weatherImgNameMutableLiveData.value = response?.weather?.get(0)?.main
            }
        }
    }
}
