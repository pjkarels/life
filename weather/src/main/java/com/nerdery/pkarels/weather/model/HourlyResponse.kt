package com.nerdery.pkarels.weather.model

import com.google.gson.annotations.SerializedName

class HourlyResponse(hours: List<ForecastCondition>) {
    /**
     * Ordered List of Hourly [ForecastCondition]
     * @return List of [ForecastCondition]
     */
    @SerializedName("data")
    var hours: List<ForecastCondition>
        internal set

    init {
        this.hours = hours
    }
}
