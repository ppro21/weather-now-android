package com.pprodev.weathernow.network

import com.pprodev.weathernow.model.CurrentWeatherResponseModel
import retrofit2.http.GET

interface WeatherApiService {
    @GET("weather?q=Wellington,nz&units=metric&appid={weather_api_key}")
    suspend fun getCurrentWeather(): CurrentWeatherResponseModel
}