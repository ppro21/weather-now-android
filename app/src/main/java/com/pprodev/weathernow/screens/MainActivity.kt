package com.pprodev.weathernow.screens

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.pprodev.weathernow.R
import com.pprodev.weathernow.service.AppLocationService
import com.pprodev.weathernow.utils.PermissionUtil
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    // ViewModel
    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    private lateinit var weatherImgName: TextView
    private lateinit var currentCityName: TextView
    private lateinit var btnRefresh: Button
    private lateinit var weatherImg: AppCompatImageView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val appLocationService = AppLocationService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.d("GPS Service Running: %s", appLocationService.isServiceRunning())

        // Views
        currentCityName = findViewById(R.id.tvCityName)
        weatherImg = findViewById(R.id.imgWeatherIcon)
        weatherImgName = findViewById(R.id.tvWeatherImgName)
        btnRefresh = findViewById(R.id.btnRefresh)

        // FusedLocationProvider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request permission
        requestPermission()
        fetchCurrentLocation() //

        // Observe LiveData
        observeViewModel()

        btnRefresh.setOnClickListener {
            viewModel.getCurrentWeather("Abu Dhabi")
        }
    }

    private fun observeViewModel() {
        // weather image loader
        viewModel.weatherIconLiveData.observe(this) { weatherIconLiveData ->
            weatherImg.load(weatherIconLiveData)
        }

        // City Name
        viewModel.cityNameLiveData.observe(this) { cityNameLiveData ->
            cityNameLiveData?.let {
                currentCityName.text = cityNameLiveData
            }
        }

        // Weather img name
        viewModel.weatherImgNameLiveData.observe(this) { weatherImgNameLiveData ->
            weatherImgName.text = weatherImgNameLiveData
        }

    }


    // PERMISSION
    private fun requestPermission() {
        // If already has permission
        if (PermissionUtil.hasLocationPermissions(this.applicationContext)) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "Please grant permission in order for the app to have full functionality.",
                0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Please grant permission in order for the app to have full functionality.",
                0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // Fetch current location's weather
    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        if (PermissionUtil.hasLocationPermissions(this)) {
            val locationReq = LocationRequest.PRIORITY_HIGH_ACCURACY
            val cancellationToken = CancellationTokenSource().token
            val geocoder = Geocoder(this@MainActivity)

            fusedLocationClient.getCurrentLocation(locationReq, cancellationToken)
                .addOnSuccessListener { location ->

                    if (location != null) {
                        Timber.i(
                            "Current Location: Lat=%s , Lon=%s",
                            location.latitude,
                            location.longitude
                        )

                        // GET area name from lat-lon
                        val areaList: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val areaName = areaList[0].locality

                        // GET weather of city
                        viewModel.getCurrentWeather(areaName)
                        Timber.i("Area Name/ City Name = %s", areaName)
                    }
                }
                .addOnFailureListener {
                    Timber.e("Error: %s", it.localizedMessage)
                    Timber.e("Error: %s", it.printStackTrace())
                }

        }
    }

}
