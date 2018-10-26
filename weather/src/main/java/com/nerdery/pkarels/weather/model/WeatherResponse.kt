package com.nerdery.pkarels.weather.model

import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.nerdery.pkarels.life.model.ForecastCondition

/**
 * Response from DarkSky weather requests in [com.nerdery.pkarels.weather.data.api.WeatherService]
 */
class WeatherResponse(@field:SerializedName("currently")
                      /**
                       * Current Weather Condition
                       * @return ForecastCondition
                       */
                      val currentForecast: ForecastCondition,
                      /**
                       * Hourly Response model that contains list of ForecastConditions
                       * @return HourlyResponse
                       */
                      val hourly: HourlyResponse) {
    @PrimaryKey
    lateinit var forecasts: List<DayForecasts>
}
