package com.bitbybitlabs.weather.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Response from DarkSky weather requests in [com.bitbybitlabs.weather.data.api.WeatherService]
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
