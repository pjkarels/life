package com.nerdery.pkarels.weather.model

import com.google.gson.annotations.SerializedName

data class HourlyResponse constructor(
        /**
         * Ordered List of Hourly [ForecastCondition]
         * @return List of [ForecastCondition]
         */
        @SerializedName("data") val hours: List<ForecastCondition>) {
}
