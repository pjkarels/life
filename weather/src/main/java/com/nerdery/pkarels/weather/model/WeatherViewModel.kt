package com.nerdery.pkarels.weather.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import com.nerdery.pkarels.life.LifeApplication
import com.nerdery.pkarels.life.TempUnit
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.weather.data.IconLoadedListener
import com.nerdery.pkarels.weather.entity.CurrentConditionEntity
import com.nerdery.pkarels.weather.entity.HourlyForecastsEntity
import com.nerdery.pkarels.weather.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.ZoneOffset
import timber.log.Timber
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

    private lateinit var dayForecasts: ArrayList<DayForecasts>

    private val currentConditions = MutableLiveData<ForecastCondition>()
    private val dayHourlyForecasts = MutableLiveData<List<DayForecasts>>()

    fun currentConditions(): LiveData<ForecastCondition> = currentConditions
    fun dayHourlyForecasts(): LiveData<List<DayForecasts>> = dayHourlyForecasts

    fun init(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit, lifeApplication: LifeApplication) {
        this.location = zipLocation
        this.tempUnit = tempUnit
        repository = WeatherRepository(lifeApplication)

        refreshWeather(zipLocation, tempUnit)
    }

    fun refreshWeather(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit) {
        repository.getWeather(zipLocation, tempUnit)
                .doOnSuccess {
                    repository.getCurrentConditionsFromDb(zipLocation.zipCode)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onCurrentConditionsFromDb(it) },
                                    Timber::e)
                    repository.getHourlyConditionsFromDb(zipLocation.zipCode)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ onHourlyConditionsFromDb(it) },
                                    Timber::e)
                }
                .subscribe()
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

    private fun onCurrentConditionsFromDb(currentConditionEntities: List<CurrentConditionEntity>) {
        if (currentConditionEntities.isNotEmpty()) {
            val currentConditionsEntity = currentConditionEntities[0] // even if there are more than 1, we only want the first
            currentConditions.value = ForecastCondition(
                    currentConditionsEntity.weatherCondition.summary,
                    currentConditionsEntity.weatherCondition.icon,
                    currentConditionsEntity.weatherCondition.temp,
                    currentConditionsEntity.weatherCondition.time.toEpochSecond(ZoneOffset.UTC)
            )
        }
    }

    private fun onHourlyConditionsFromDb(hourlyResponse: List<HourlyForecastsEntity>) {
        dayHourlyForecasts.value = processWeather(hourlyResponse)
    }

    /***
     * Divides hourly forecast list into daily forecast blocks
     */
    private fun processWeather(hourlyResponse: List<HourlyForecastsEntity>): List<DayForecasts> {
        val forecasts = ArrayList<DayForecasts>()
//        val hourlyResponse = response.hourly
        val hours = hourlyResponse.map { hourlyForecastsEntity ->
            ForecastCondition(
                    summary = hourlyForecastsEntity.weatherCondition.summary,
                    icon = hourlyForecastsEntity.weatherCondition.icon,
                    temp = hourlyForecastsEntity.weatherCondition.temp,
                    time = hourlyForecastsEntity.weatherCondition.time.toEpochSecond(ZoneOffset.UTC)
            )
        } //hourlyResponse.hours
        var conditions: MutableList<ForecastCondition> = ArrayList()
        var dayForecast = DayForecasts()
        val now = Calendar.getInstance(Locale.US)
        for (condition in hours) {
            condition.tempUnit = this.tempUnit
            val then = Calendar.getInstance(Locale.US)
            then.timeInMillis = condition.getTimeInMillis()
            if (then.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
                conditions.add(condition)
            } else {
                now.add(Calendar.DAY_OF_MONTH, 1) // increment one day
                dayForecast.conditions = conditions
                forecasts.add(dayForecast)

                dayForecast = DayForecasts()
                conditions = ArrayList()
                conditions.add(condition)
            }
        }
        dayForecast.conditions = conditions
        forecasts.add(dayForecast)
        for (forecast in forecasts) {
            var lowest = 999.0
            var highest = -999.0
            for (condition in forecast.conditions) {
                if (condition.temp > highest) highest = condition.temp // record highest temp
                if (condition.temp < lowest) lowest = condition.temp // record lowest temp
            }
            for (condition in forecast.conditions) {
                if (condition.temp == lowest)
                    condition.isLowest = true
                if (condition.temp == highest)
                    condition.isHighest = true
            }
        }
//        response.forecasts = forecasts

        return forecasts
    }
}
