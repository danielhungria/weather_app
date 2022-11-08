package com.example.weatherapp.services

import com.example.weatherapp.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET

const val city: String = "Salvador"
const val country: String = "br"
const val unit: String = "metric"
const val chaveAPI: String = "369ebfe23294031f706b1c4de95efc30"

interface WeatherAPI {

    //https://api.openweathermap.org/data/2.5/weather?q=Salvador,br&appid=369ebfe23294031f706b1c4de95efc30&units=metric

    //https://api.openweathermap.org/data/2.5/weather?q=Sao%20Paulo,br&units=metric&appid=369ebfe23294031f706b1c4de95efc30

    @GET("data/2.5/weather?q=${city},${country}&units=${unit}&appid=${chaveAPI}")

    fun getData(): Call<WeatherModel>

}