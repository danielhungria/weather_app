package com.example.weatherapp.activity

import android.graphics.drawable.Drawable
import com.example.weatherapp.R
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.services.WeatherAPI
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://api.openweathermap.org/"

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraSwipeRefresh()
        lifecycleScope.launch {
            getMyData()
        }
    }

    private fun configuraSwipeRefresh() {
        binding.activityMainSwipeRefresh.setOnRefreshListener {
            lifecycleScope.launch{
                getMyData()
                binding.activityMainSwipeRefresh.isRefreshing = false
            }
        }
    }


    fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(
                call: Call<WeatherModel>,
                response: Response<WeatherModel>
            ) {
                if (response.isSuccessful) {
                    Log.i("APIConnect", "Success")
                    setInfo(response)
                } else {
                    Log.i("APIConnect", "Error")
                }
            }

            override fun onFailure(
                call: Call<WeatherModel>,
                t: Throwable
            ) {
                Log.d("APIConnect", "onFailure: " + t.message)

            }
        })

    }

    private fun setInfo(response: Response<WeatherModel>) {
        val sunrise = response.body()?.sys?.sunrise?.toLong()!!
        val sunriseFormat = SimpleDateFormat("hh:mm a").format(Date(sunrise*1000))
        val sunset = response.body()?.sys?.sunset?.toLong()!!
        val sunsetFormat = SimpleDateFormat("hh:mm a").format(Date(sunset*1000))
        val status = response.body()?.weather?.get(0)?.description
        val windSpeed = response.body()?.wind?.speed!!
        val windSpeedKm = windSpeed * (3.6)
        val humidity = response.body()?.main?.humidity
        val tempPadrao = response.body()?.main?.temp
        val tempMax = response.body()?.main?.temp_max
        val tempMin = response.body()?.main?.temp_min
        val locale = response.body()?.name
        val pressure = response.body()?.main?.pressure
        val updated = response.body()?.dt?.toLong()!!
        val updatedFormat = SimpleDateFormat("yyyy.MM.dd hh:mm a").format(Date(updated*1000))

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
        binding.tempMax.text = "Temp Max: " + tempMax2+ "°C"
        binding.tempMin.text = "Temp Min: " + tempMin2 + "°C"
        binding.adress.text = locale.toString()
        binding.pressure.text = pressure.toString()
        binding.updated.text = "Updated at: " +updatedFormat

        //SET IMAGE
        val image = binding.statusImageView

        if (status=="clear sky"){
            image.setImageResource(R.drawable.sun)
        }
        if (status=="few clouds"){
            image.setImageResource(R.drawable.few_clouds)
        }
        if (status=="overcast clouds" || status=="scattered clouds" || status=="broken clouds"){
            image.setImageResource(R.drawable.overcast_clouds)
        }
        if (status=="extreme rain"){
            image.setImageResource(R.drawable.rain2)
        }
        if (status=="light rain"){
            image.setImageResource(R.drawable.rain)
        }

        Log.i("FUN SETINFO", "setInfo: success. updated: $updatedFormat")

    }



}