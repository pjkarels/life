package com.bitbybitlabs.weather.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import org.threeten.bp.LocalDateTime

@Entity(
        tableName = "hour_forecasts",
        primaryKeys = ["zipcode", "date"]
)
data class HourlyForecastsEntity(

        @ColumnInfo(name = "zipcode")
        val zipcode: Long,

        @ColumnInfo(name = "date")
        val date: LocalDateTime,

        @Embedded
        val weatherCondition: WeatherConditionEntity
)