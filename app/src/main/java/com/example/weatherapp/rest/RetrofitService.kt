package com.example.weatherapp.rest

import com.example.weatherapp.model.WeatherModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val city: String = "Salvador"
const val country: String = "br"
const val unit: String = "metric"
const val chaveAPI: String = "369ebfe23294031f706b1c4de95efc30"
const val BASE_URL = "https://api.openweathermap.org/"


interface RetrofitService {

    @GET("data/2.5/weather?q=$city,$country&units=$unit&appid=$chaveAPI")
    fun getWeatherData(): Call<WeatherModel>

    companion object{
        private val retrofitService: RetrofitService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("$BASE_URL")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(RetrofitService::class.java)
        }
        fun getInstance(): RetrofitService{
            return retrofitService
        }
    }

}