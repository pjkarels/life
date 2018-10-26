package com.nerdery.pkarels.weather.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.graphics.Bitmap
import com.nerdery.pkarels.life.LifeApplication
import com.nerdery.pkarels.life.TempUnit
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.weather.data.IconLoadedListener
import com.nerdery.pkarels.weather.entity.CurrentEntity
import com.nerdery.pkarels.weather.repository.WeatherRepository
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    //    init {
//        val weatherComponent = DaggerWeatherComponent.builder()
//                .appModule(AppModule(application))
//                .roomModule(RoomModule(application))
//                .build()
//                .inject(this)
//    }
//
    private lateinit var repository: WeatherRepository
    private lateinit var location: ZipCodeService.ZipLocation
    private lateinit var tempUnit: TempUnit
    lateinit var weatherResponseContainer: LiveData<CurrentEntity>

    private lateinit var dayForecasts: ArrayList<DayForecasts>

    fun init(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit, lifeApplication: LifeApplication) {
        this.location = zipLocation
        this.tempUnit = tempUnit
        repository = WeatherRepository(lifeApplication)

        dayForecasts = ArrayList()

        weatherResponseContainer = getWeather()
    }

    fun getWeather(): LiveData<CurrentEntity> {
        return repository.getWeather(location, tempUnit)
    }

    fun getIcon(image: String, highlighted: Boolean, listener: IconLoadedListener) {
        repository.getIcon(image, highlighted, object : IconLoadedListener {
            override fun onIconLoaded(image: Bitmap) {
                listener.onIconLoaded(image)
            }

            override fun onIconLoadedError(message: String) {
                listener.onIconLoadedError(message)
            }
        })
    }
}
