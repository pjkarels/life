package com.bitbybitlabs.weather.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import com.bitbybitlabs.life.LifeApplication
import com.bitbybitlabs.life.TempUnit
import com.bitbybitlabs.life.ZipCodeService
import com.bitbybitlabs.weather.data.IconLoadedListener
import com.bitbybitlabs.weather.di.AppModule
import com.bitbybitlabs.weather.di.DaggerWeatherComponent
import com.bitbybitlabs.weather.di.RoomModule
import com.bitbybitlabs.weather.entity.CurrentConditionEntity
import com.bitbybitlabs.weather.entity.HourlyForecastsEntity
import com.bitbybitlabs.weather.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.ZoneOffset
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    init {
        DaggerWeatherComponent.builder()
                .appModule(AppModule(application as LifeApplication))
                .roomModule(RoomModule(application))
                .build()
                .inject(this)
    }

    @Inject
    lateinit var repository: WeatherRepository

    private lateinit var location: ZipCodeService.ZipLocation
    private lateinit var tempUnit: TempUnit

    private val currentConditions = MutableLiveData<ForecastCondition>()
    private val dayHourlyForecasts = MutableLiveData<List<DayForecasts>>()
    private val weatherResponseError = MutableLiveData<WeatherResponseError>()

    fun currentConditions(): LiveData<ForecastCondition> = currentConditions
    fun dayHourlyForecasts(): LiveData<List<DayForecasts>> = dayHourlyForecasts
    fun weatherError(): LiveData<WeatherResponseError> = weatherResponseError

    fun init(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit) {
        this.location = zipLocation
        this.tempUnit = tempUnit

        refreshWeather(zipLocation, tempUnit)
    }

    fun refreshWeather(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit) {
        repository.getWeather(zipLocation, tempUnit)
                .observeOn(AndroidSchedulers.mainThread())
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
                .doOnError {
                    onWeatherError(it.localizedMessage)
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

    private fun onWeatherError(message: String) {
        weatherResponseError.value = WeatherResponseError(message)
    }

    /***
     * Divides hourly forecast list into daily forecast blocks
     */
    private fun processWeather(hourlyResponse: List<HourlyForecastsEntity>): List<DayForecasts> {
        val forecasts = ArrayList<DayForecasts>()
        val hours = hourlyResponse.map { hourlyForecastsEntity ->
            ForecastCondition(
                    summary = hourlyForecastsEntity.weatherCondition.summary,
                    icon = hourlyForecastsEntity.weatherCondition.icon,
                    temp = hourlyForecastsEntity.weatherCondition.temp,
                    time = hourlyForecastsEntity.weatherCondition.time.toEpochSecond(ZoneOffset.UTC)
            )
        }
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

        return forecasts
    }
}
