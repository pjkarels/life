package com.nerdery.pkarels.weather.repository

import android.app.Application
import android.graphics.BitmapFactory
import com.nerdery.pkarels.life.LifeApplication
import com.nerdery.pkarels.life.TempUnit
import com.nerdery.pkarels.life.Util
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.weather.data.IconLoadedListener
import com.nerdery.pkarels.weather.data.IconService
import com.nerdery.pkarels.weather.data.WeatherDatabase
import com.nerdery.pkarels.weather.data.WeatherService
import com.nerdery.pkarels.weather.entity.CurrentConditionEntity
import com.nerdery.pkarels.weather.entity.HourlyForecastsEntity
import com.nerdery.pkarels.weather.entity.WeatherConditionEntity
import com.nerdery.pkarels.weather.model.WeatherResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.threeten.bp.LocalDateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class WeatherRepository @Inject constructor(application: LifeApplication) {

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

    fun getWeather(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit): Single<WeatherResponse> {
        this.tempUnit = tempUnit
        return refreshWeather(zipLocation, tempUnit)
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

    fun getCurrentConditionsFromDb(zip: Long) = weatherResponseDao.loadCurrentConditions(zip)
    fun getHourlyConditionsFromDb(zip: Long) = weatherResponseDao.loadHourlyConditions(zip)

    private fun refreshWeather(zipLocation: ZipCodeService.ZipLocation, tempUnit: TempUnit): Single<WeatherResponse> {
        Single.fromCallable {
            weatherResponseDao.deleteCurrentConditions()
            weatherResponseDao.deleteHourlyConditions()
        }
                .subscribeOn(Schedulers.io())
                .subscribe({}, Timber::e)

        return weatherService.getWeather(zipLocation.latitude, zipLocation.longitude, tempUnit)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { weatherResponse ->
                    val weatherEntity = WeatherConditionEntity(weatherResponse.currentForecast.summary,
                            weatherResponse.currentForecast.icon,
                            weatherResponse.currentForecast.temp,
                            weatherResponse.currentForecast.getTimeAsLocalDateTime())
                    // create Entity
                    val currentEntity = CurrentConditionEntity(zipLocation.zipCode,
                            LocalDateTime.now(),
                            weatherEntity)

                    val hourlyForecasts = weatherResponse.hourly.hours.map { hourlyForecast ->
                        HourlyForecastsEntity(
                                zipcode = zipLocation.zipCode,
                                date = hourlyForecast.getTimeAsLocalDateTime(),
                                weatherCondition = WeatherConditionEntity(
                                        summary = hourlyForecast.summary,
                                        icon = hourlyForecast.icon,
                                        temp = hourlyForecast.temp,
                                        time = hourlyForecast.getTimeAsLocalDateTime()
                                )
                        )
                    }
                    weatherResponseDao.saveCurrentConditions(currentEntity)
                    weatherResponseDao.saveHourlyConditions(hourlyForecasts)
                }
                .doOnError {

                }
    }
}
