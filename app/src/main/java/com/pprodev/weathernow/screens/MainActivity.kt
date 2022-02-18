package com.pprodev.weathernow.screens

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.pprodev.weathernow.R

class MainActivity : AppCompatActivity() {

    // ViewModel
    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    private lateinit var textView: TextView
    private lateinit var btnRefresh: Button
    private lateinit var iconWeather: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectedCity = viewModel.cityNameLiveData.value ?: "Wellington"

        iconWeather = findViewById(R.id.imgIcon)
        textView = findViewById(R.id.tvHello)
        btnRefresh = findViewById(R.id.btnRefresh)

        viewModel.getCurrentWeather(selectedCity)

        // Observe LiveData
        observeViewModel()


        btnRefresh.setOnClickListener {
            viewModel.getCurrentWeather("Madrid")
        }
    }

    private fun observeViewModel() {
        viewModel.cityNameLiveData.observe(this) { cityName ->
            cityName?.let {
                textView.text = cityName
            }
        }

        // weather image loader
        viewModel.weatherIconLiveData.observe(this) { weatherIcon ->
            iconWeather.load(weatherIcon)
        }
    }

}