package com.nerdery.pkarels.weather.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Specific weather condition for time and location
 */
data class ForecastCondition(val summary: String,
                             val icon: String,
                             @field:SerializedName("temperature")
                             internal var temp: Double,
                             private var time: Long) {

    var isLowest: Boolean = false
    var isHightest: Boolean = false
    fun getTemp(): String {
        return temp.toString()
    }
    /**
     * Time/Date of Forecast Condition
     * @return Date
     */
    fun getTime(): Date {
        return Date(time)
    }
}
