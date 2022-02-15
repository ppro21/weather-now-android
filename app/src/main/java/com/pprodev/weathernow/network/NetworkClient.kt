package com.pprodev.weathernow.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkClient {

    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // Moshi
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    // Retrofit
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    // ApiClient
    val apiClient = retrofit.create(WeatherApiService::class.java)

}