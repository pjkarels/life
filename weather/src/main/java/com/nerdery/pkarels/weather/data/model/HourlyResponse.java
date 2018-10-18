package com.nerdery.pkarels.weather.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HourlyResponse {
    @SerializedName("data")
    List<ForecastCondition> hours;

    public HourlyResponse(List<ForecastCondition> hours) {
        this.hours = hours;
    }

    /**
     * Ordered List of Hourly {@link ForecastCondition}
     * @return List of {@link ForecastCondition}
     */
    public List<ForecastCondition> getHours() {
        return hours;
    }
}
