package com.nerdery.pkarels.weather.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.weather.repository.WeatherRepository

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var repository: WeatherRepository
    lateinit var weatherResponse: LiveData<WeatherResponse>
    private lateinit var location: ZipCodeService.ZipLocation
    private lateinit var tempUnit: TempUnit

    fun init(repository: WeatherRepository, zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit) {
        this.location = zipLocation
        this.tempUnit = tempUnit

        weatherResponse = repository.getWeather(location, tempUnit)
    }

    fun getWeather(): LiveData<WeatherResponse> {
        return repository.getWeather(location, tempUnit)
    }
}
