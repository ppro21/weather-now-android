package com.pprodev.weathernow.service

import android.app.Activity
import android.content.Context
import android.location.LocationManager


class AppLocationService(private val application: Activity?) {

    fun isServiceRunning(): Boolean {
        val locationManager =
            application?.applicationContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}