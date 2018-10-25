package com.nerdery.pkarels.weather.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Response from DarkSky weather requests in [com.nerdery.pkarels.weather.data.api.WeatherService]
 */
@Entity
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
    private var id = 0
    lateinit var forecasts: List<DayForecasts>
}
