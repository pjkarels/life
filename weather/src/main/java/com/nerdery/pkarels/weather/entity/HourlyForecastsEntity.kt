package com.nerdery.pkarels.weather.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
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