package com.pprodev.weathernow.service

import android.content.Context
import android.location.LocationManager
import androidx.fragment.app.FragmentActivity


class AppLocationService(private val application: FragmentActivity) {

    fun isServiceRunning(): Boolean {
        val locationManager =
            application.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}