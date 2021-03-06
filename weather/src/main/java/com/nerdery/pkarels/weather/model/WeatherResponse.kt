package com.nerdery.pkarels.weather.model

import com.google.gson.annotations.SerializedName

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
    lateinit var forecasts: DayForecasts
}
