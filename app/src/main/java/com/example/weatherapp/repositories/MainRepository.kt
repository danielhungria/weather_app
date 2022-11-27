package com.example.weatherapp.repositories

import com.example.weatherapp.rest.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getWeatherData() = retrofitService.getWeatherData()

}