package com.nerdery.pkarels.weather.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.graphics.BitmapFactory
import com.nerdery.pkarels.life.LifeApplication
import com.nerdery.pkarels.life.TempUnit
import com.nerdery.pkarels.life.Util
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.weather.data.IconLoadedListener
import com.nerdery.pkarels.weather.data.IconService
import com.nerdery.pkarels.weather.data.WeatherDatabase
import com.nerdery.pkarels.weather.data.WeatherService
import com.nerdery.pkarels.weather.entity.CurrentEntity
import com.nerdery.pkarels.weather.model.DayForecasts
import com.nerdery.pkarels.weather.model.ForecastCondition
import com.nerdery.pkarels.weather.model.WeatherResponse
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class WeatherRepository(application: LifeApplication) {

    private lateinit var tempUnit: TempUnit

    private var weatherService: WeatherService =
            Util.provideRetrofit(application.apiServicesProvider.client,
                    WeatherService.URL,
                    Util.provideGson()).create(WeatherService::class.java)
    private var iconService: IconService =
            Util.provideRetrofit(application.apiServicesProvider.client,
                    IconService.URL,
                    Util.provideGson()).create(IconService::class.java)

    private val database: WeatherDatabase = WeatherDatabase.create(application as Application)
    private val weatherResponseDao = database.weatherResponseDao()

    fun getWeather(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit): LiveData<CurrentEntity> {
        this.tempUnit = tempUnit
        refreshWeather(zipLocation, tempUnit)

        return weatherResponseDao.load()
    }

    fun getIcon(image: String, isSelected: Boolean, listener: IconLoadedListener) {
        val selected = if (isSelected) "-selected" else ""

        iconService.getIconCall(image, selected).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val byteArray = response.body()?.bytes()
                if (byteArray != null) {
                    val icon = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    listener.onIconLoaded(icon)
                } else {
                    onFailure(call, IOException("Unable to load icon"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onIconLoadedError(t.localizedMessage)
                call.cancel()
            }
        })
    }

    private fun refreshWeather(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit) {
        weatherService.getWeather(zipLocation.latitude, zipLocation.longitude, tempUnit)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { weatherResponse ->
                    val processedWeather = processWeather(weatherResponse)
                    // create Entity
                    val currentEntity = CurrentEntity(zipLocation.zipCode)
                    weatherResponseDao.save(currentEntity)
            }
                .subscribe()

//        executor.execute {
//            val responseBody = weatherService.getWeatherCall(zipLocation.latitude, zipLocation.longitude, tempUnit).execute().body()
//            if (responseBody != null) {
//                val modifiedResponse = processWeather(responseBody)
//                weatherResponseDao.save(modifiedResponse)
//            }
//        }
    }

    /***
     * Divides hourly forecast list into daily forecast blocks
     */
    private fun processWeather(response: WeatherResponse): WeatherResponse {
        val forecasts = ArrayList<DayForecasts>()
        val hourlyResponse = response.hourly
        val hours = hourlyResponse.hours
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
        response.forecasts = forecasts

        return response
    }
}
