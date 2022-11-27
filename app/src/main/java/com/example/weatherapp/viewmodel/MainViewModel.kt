package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.repositories.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository): ViewModel() {

    val listWeatherModel = MutableLiveData<WeatherModel>()
    val errorMessage = MutableLiveData<String>()

    fun getWeatherData(){

        val request = repository.getWeatherData()
        request.enqueue(object : Callback<WeatherModel>{
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                //quando houver uma resposta
                listWeatherModel.postValue(response.body())
                Log.i("MainViewModel", "onResponse:")
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                //quando houver falha
                errorMessage.postValue(t.message)
             }
        })

    }

}