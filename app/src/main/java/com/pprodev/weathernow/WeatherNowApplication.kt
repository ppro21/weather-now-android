package com.pprodev.weathernow

import android.app.Application
import timber.log.Timber

class WeatherNowApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}