package com.pprodev.weathernow.repository

import com.pprodev.weathernow.model.CurrentWeatherResponseModel
import com.pprodev.weathernow.network.NetworkClient
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * Repository provides clean API abstraction over data / resource
 * */
class SharedRepository {
    suspend fun getCurrentWeather(cityName: String): CurrentWeatherResponseModel? {
        val request = try {
            NetworkClient.apiClient.getCurrentWeather(cityName)
        } catch (e: IOException) {
            Timber.e(e.localizedMessage)
            return null
        } catch (e: HttpException) {
            Timber.e(e.localizedMessage)
            return null
        }
        return request
    }
}