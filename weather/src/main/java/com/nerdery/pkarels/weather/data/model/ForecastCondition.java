package com.nerdery.pkarels.weather.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Specific weather condition for time and location
 */
public class ForecastCondition {
    String summary;
    String icon;
    @SerializedName("temperature")
    double temp;
    long time;

    private boolean isLowest;
    private boolean isHightest;

    public ForecastCondition(String summary, String icon, double temp, long time) {
        this.summary = summary;
        this.icon = icon;
        this.temp = temp;
        this.time = time;
    }

    /**
     * Text summary of weather condition
     * @return Summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Icon name of weather condition
     * @return Icon Name
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Temperature in degrees of {@link TempUnit} sent during request
     * @return Temperature
     */
    public long getTemp() {
        return Math.round(temp);
    }

    /**
     * Time/Date of Forecast Condition
     * @return Date
     */
    public Date getTime() {
        return new Date(time);
    }

    public boolean isLowest() {
        return isLowest;
    }

    public void setLowest(boolean lowest) {
        isLowest = lowest;
    }

    public boolean isHightest() {
        return isHightest;
    }

    public void setHightest(boolean hightest) {
        isHightest = hightest;
    }
}
