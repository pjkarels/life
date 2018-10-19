package com.nerdery.pkarels.weather.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Specific weather condition for time and location
 */
class ForecastCondition(internal var summary: String,
                        internal var icon: String,
                        @field:SerializedName("temperature")
                        internal var temp: Double,
                        private var time: Long) {

    var isLowest: Boolean = false
    var isHightest: Boolean = false
    /**
     * Time/Date of Forecast Condition
     * @return Date
     */
    fun getTime(): Date {
        return Date(time)
    }
}
