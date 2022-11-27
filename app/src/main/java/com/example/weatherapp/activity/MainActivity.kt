package com.example.weatherapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.repositories.MainRepository
import com.example.weatherapp.rest.RetrofitService
import com.example.weatherapp.viewmodel.MainViewModel
import com.example.weatherapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraSwipeRefresh()
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(MainRepository(retrofitService))).get(
                MainViewModel::class.java
            )
    }

    override fun onStart() {
        super.onStart()

        viewModel.listWeatherModel.observe(this, Observer { weather ->
            setInfo(weather)
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.getWeatherData()
    }

    private fun configuraSwipeRefresh() {
        binding.activityMainSwipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getWeatherData()
                binding.activityMainSwipeRefresh.isRefreshing = false
            }
        }
    }


    private fun setInfo(weatherModel: WeatherModel) {
        val sunrise = weatherModel.sys?.sunrise?.toLong()!!
        val sunriseFormat = SimpleDateFormat("hh:mm a").format(Date(sunrise * 1000))
        val sunset = weatherModel.sys?.sunset?.toLong()!!
        val sunsetFormat = SimpleDateFormat("hh:mm a").format(Date(sunset * 1000))
        val status = weatherModel.weather?.get(0)?.description
        val windSpeed = weatherModel.wind?.speed!!
        val windSpeedKm = windSpeed * (3.6)
        val humidity = weatherModel.main?.humidity
        val tempPadrao = weatherModel.main?.temp
        val tempMax = weatherModel.main?.temp_max
        val tempMin = weatherModel.main?.temp_min
        val locale = weatherModel.name
        val pressure = weatherModel.main?.pressure
        val updated = weatherModel.dt?.toLong()!!
        val updatedFormat = SimpleDateFormat("yyyy.MM.dd hh:mm a").format(Date(updated * 1000))

        //ROUNDED
        val tempPadrao2 = String.format("%.0f", tempPadrao)
        val tempMin2 = String.format("%.0f", tempMin)
        val tempMax2 = String.format("%.0f", tempMax)
        val windSpeedKm2 = String.format("%.0f", windSpeedKm)

        //BINDING
        binding.tempPadrao.text = tempPadrao2 + "°C"
        binding.wind.text = windSpeedKm2 + " km/h"
        binding.sunrise.text = sunriseFormat
        binding.sunset.text = sunsetFormat
        binding.status.text = status.toString()
        binding.humidity.text = humidity.toString() + " %"
        binding.tempMax.text = "Temp Max: " + tempMax2 + "°C"
        binding.tempMin.text = "Temp Min: " + tempMin2 + "°C"
        binding.adress.text = locale.toString()
        binding.pressure.text = pressure.toString()
        binding.updated.text = "Updated at: " + updatedFormat

        //SET IMAGE
        val image = binding.statusImageView

        if (status == "clear sky") {
            image.setImageResource(R.drawable.sun)
        }
        if (status == "few clouds") {
            image.setImageResource(R.drawable.few_clouds)
        }
        if (status == "overcast clouds" || status == "scattered clouds" || status == "broken clouds") {
            image.setImageResource(R.drawable.overcast_clouds)
        }
        if (status == "extreme rain") {
            image.setImageResource(R.drawable.rain2)
        }
        if (status == "light rain") {
            image.setImageResource(R.drawable.rain)
        }
    }


}