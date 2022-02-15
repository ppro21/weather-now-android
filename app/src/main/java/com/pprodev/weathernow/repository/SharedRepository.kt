package com.pprodev.weathernow.repository

import com.pprodev.weathernow.model.CurrentWeatherResponseModel
import com.pprodev.weathernow.network.NetworkClient
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class SharedRepository {
    suspend fun getCurrentWeather(): CurrentWeatherResponseModel? {
        val request = try {
            NetworkClient.apiClient.getCurrentWeather()
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