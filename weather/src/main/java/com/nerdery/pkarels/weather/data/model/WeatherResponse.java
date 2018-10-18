package com.nerdery.pkarels.weather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response from DarkSky weather requests in {@link com.nerdery.pkarels.weather.data.api.WeatherService}
 */
public class WeatherResponse {

    @SerializedName("currently")
    private ForecastCondition currentForecast;

    private HourlyResponse hourly;

    public WeatherResponse(ForecastCondition currentForecast, HourlyResponse hourly) {
        this.currentForecast = currentForecast;
        this.hourly = hourly;
    }

    /**
     * Current Weather Condition
     * @return ForecastCondition
     */
    public ForecastCondition getCurrentForecast() {
        return currentForecast;
    }

    /**
     * Hourly Response model that contains list of ForecastConditions
     * @return HourlyResponse
     */
    public HourlyResponse getHourly() {
        return hourly;
    }
}
