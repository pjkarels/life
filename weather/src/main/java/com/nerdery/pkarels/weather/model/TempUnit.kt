package com.nerdery.pkarels.weather.model

import com.google.gson.annotations.SerializedName

/**
 * Temperature unit to be used in requests for [com.nerdery.pkarels.weather.data.api.WeatherService]
 */
enum class TempUnit constructor(private val value: String) {
    @SerializedName("si")
    CELSIUS("si"),

    @SerializedName("us")
    FAHRENHEIT("us");

    override fun toString(): String {
        return value
    }
}
