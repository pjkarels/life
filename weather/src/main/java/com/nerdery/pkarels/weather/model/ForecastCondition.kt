package com.nerdery.pkarels.weather.model

import com.google.gson.annotations.SerializedName
import com.nerdery.pkarels.life.Util
import java.util.*

/**
 * Specific weather condition for time and location
 */
data class ForecastCondition(val summary: String,
                             val icon: String,
                             @field:SerializedName("temperature")
                             internal var temp: Double,
                             private val time: Long) {

    var isLowest: Boolean = false
    var isHighest: Boolean = false
    fun getTemp(): String {
        return Util.round(temp, 1)
    }

    fun getTime(): Long {
        return time * 1000
    }

    /**
     * Time/Date of Forecast Condition
     * @return Date
     */
    fun getTimeAsDate(): Date {
        return Date(time * 1000)
    }
}
