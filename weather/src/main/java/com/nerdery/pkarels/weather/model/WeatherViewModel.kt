package com.nerdery.pkarels.weather.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.weather.repository.WeatherRepository
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var repository: WeatherRepository
    lateinit var weatherResponseContainer: LiveData<WeatherResponse>
    private lateinit var location: ZipCodeService.ZipLocation
    private lateinit var tempUnit: TempUnit

    private lateinit var dayForecasts: ArrayList<DayForecasts>

    fun init(repository: WeatherRepository, zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit) {
        this.repository = repository
        this.location = zipLocation
        this.tempUnit = tempUnit

        dayForecasts = ArrayList()

        weatherResponseContainer = repository.getWeather(location, tempUnit)
    }

    fun getWeather(): LiveData<WeatherResponse> {
        return repository.getWeather(location, tempUnit)
    }

    fun getIcon(image: String, highlighted: Boolean = false): IconResponse {
        return repository.getIcon(image, highlighted)
    }
}
