package com.pprodev.weathernow.screens.fragment

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.pprodev.weathernow.databinding.FragmentHomeBinding
import com.pprodev.weathernow.screens.SharedViewModel
import com.pprodev.weathernow.service.AppLocationService
import com.pprodev.weathernow.utils.PermissionUtil
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val appLocationService = AppLocationService(this.requireActivity())
        Timber.i("GPS Service Running: %s", appLocationService.isServiceRunning())
        if (appLocationService.isServiceRunning()) {
            fetchCurrentLocation()
            observeViewModel()
        } else {
            Toast.makeText(
                this.requireContext(),
                "Turn ON your GPS and Internet.",
                Toast.LENGTH_LONG
            ).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Observers of LiveData objects in SharedViewModel
     * */
    private fun observeViewModel() {
        // weather image loader
        viewModel.weatherIconLiveData.observe(this.viewLifecycleOwner) { weatherIconLiveData ->
            binding.imgWeatherIcon.load(weatherIconLiveData)
        }

        // City Name
        viewModel.cityNameLiveData.observe(this.viewLifecycleOwner) { cityNameLiveData ->
            cityNameLiveData?.let {
                binding.tvCityName.text = cityNameLiveData
            }
        }

        // Weather img name
        viewModel.weatherImgNameLiveData.observe(this.viewLifecycleOwner) { weatherImgNameLiveData ->
            binding.tvWeatherImgName.text = weatherImgNameLiveData
        }
    }

    /**
     * Fetch current location of device on App-start
     * */
    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        if (PermissionUtil.hasLocationPermissions(this.requireActivity())) {
            val locationReq = LocationRequest.PRIORITY_HIGH_ACCURACY
            val cancellationToken = CancellationTokenSource().token
            val geocoder = Geocoder(this.activity)

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

    // OnDestroy LifeCycle
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}