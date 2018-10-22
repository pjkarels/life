package com.nerdery.pkarels.weather.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.weather.data.WeatherService
import com.nerdery.pkarels.weather.model.TempUnit
import com.nerdery.pkarels.weather.model.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository {
    private lateinit var weatherService: WeatherService

    fun getWeather(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit): LiveData<WeatherResponse> {
        val data = MutableLiveData<WeatherResponse>()
        weatherService.getWeatherCall(zipLocation.latitude, zipLocation.longitude, tempUnit).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                call.cancel()
            }
        })

        return data
    }
}
