package com.nerdery.pkarels.weather.data.model;

import java.util.List;

public class DayForecasts {
    private List<ForecastCondition> conditions;

    public List<ForecastCondition> getConditions() {
        return conditions;
    }
    public void setConditions(List<ForecastCondition> conditions) {
        this.conditions = conditions;
    }
}
