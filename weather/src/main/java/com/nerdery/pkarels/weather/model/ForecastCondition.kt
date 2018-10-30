package com.nerdery.pkarels.weather.model

import com.google.gson.annotations.SerializedName
import com.nerdery.pkarels.life.TempUnit
import com.nerdery.pkarels.life.Util
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import java.util.*

/**
 * Specific weather condition for time and location
 */
data class ForecastCondition(val summary: String,
                             val icon: String,
                             @field:SerializedName("temperature")
                        val temp: Double,
                             private val time: Long) {

    var isLowest: Boolean = false
    var isHighest: Boolean = false

    var tempUnit = TempUnit.FAHRENHEIT

    // Todo - move to ViewModel
    fun getTemperature(): String {
        return Util.round(temp, 1)
    }

    // TODO: move to ViewModel
    fun getTimeInMillis(): Long {
        return time * 1000
    }

    /**
     * Time/Date of Forecast Condition
     * @return Date
     */
    fun getTimeAsDate(): Date {
        return Date(time * 1000)
    }

    fun getTimeAsLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    }
}
