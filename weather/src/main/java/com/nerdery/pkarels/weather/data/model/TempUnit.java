package com.nerdery.pkarels.weather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Temperature unit to be used in requests for {@link com.nerdery.pkarels.weather.data.api.WeatherService}
 */
public enum TempUnit {
    @SerializedName("si")
    CELSIUS("si"),

    @SerializedName("us")
    FAHRENHEIT("us");

    private String value;

    TempUnit(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
