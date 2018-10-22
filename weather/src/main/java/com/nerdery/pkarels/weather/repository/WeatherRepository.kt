package com.nerdery.pkarels.weather.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.graphics.BitmapFactory
import com.nerdery.pkarels.life.LifeApplication
import com.nerdery.pkarels.life.Util
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.weather.data.IconService
import com.nerdery.pkarels.weather.data.WeatherService
import com.nerdery.pkarels.weather.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class WeatherRepository(application: LifeApplication) {
    private var weatherService: WeatherService =
            Util.provideRetrofit(application.apiServicesProvider.client,
                    WeatherService.URL,
                    Util.provideGson()).create(WeatherService::class.java)
    private var iconService: IconService =
            Util.provideRetrofit(application.apiServicesProvider.client,
                    IconService.URL,
                    Util.provideGson()).create(IconService::class.java)

    fun getWeather(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit): LiveData<WeatherResponse> {
        val data = MutableLiveData<WeatherResponse>()
        weatherService.getWeatherCall(zipLocation.latitude, zipLocation.longitude, tempUnit).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                data.value = processWeather(response.body())
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                call.cancel()
            }
        })

        return data
    }

    fun getIcon(image: String, isSelected: Boolean): IconResponse {
        val iconResponse = IconResponse()
        val selected = if (isSelected) "-selected" else ""

        iconService.getIconCall(image, selected).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val byteArray = response.body()?.bytes()
                if (byteArray != null) {
                    iconResponse.image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
            }
        })

        return iconResponse
    }

    private fun processWeather(response: WeatherResponse?): WeatherResponse? {
        if (response != null) {
            val forecasts = ArrayList<DayForecasts>()
            val hourlyResponse = response.hourly
            val hours = hourlyResponse.hours
            var conditions: MutableList<ForecastCondition> = ArrayList()
            var dayForecast = DayForecasts()
            val now = Calendar.getInstance()
            now.time = hours.get(0).getTime()
            for (condition in hours) {
                val hourCondition = Calendar.getInstance()
                hourCondition.time = condition.getTime()
                if (hourCondition.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
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
                        condition.isHightest = true
                }
            }
            response.forecasts = forecasts
        }
        return response
    }
}
