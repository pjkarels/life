package com.nerdery.pkarels.weather.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.nerdery.pkarels.life.ZipCodeService

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    //    lateinit var weatherResponse: LiveData<WeatherResponse>
    private lateinit var location: ZipCodeService.ZipLocation
    private lateinit var tempUnit: TempUnit

    fun init(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit) {
        this.location = zipLocation
        this.tempUnit = tempUnit

    }

//    fun getWeather(): LiveData<WeatherResponse> {
//        return weatherResponse
//    }
}
