package com.pprodev.weathernow.network

import com.pprodev.weathernow.BuildConfig
import com.pprodev.weathernow.model.CurrentWeatherResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q",encoded = true) cityName: String,
        @Query("units", encoded = true) units: String = "metric",
        @Query("appid", encoded = true) apiKey: String = BuildConfig.WEATHER_API_KEY
    ): CurrentWeatherResponseModel

}