package com.bitbybitlabs.weather.entity

import android.arch.persistence.room.ColumnInfo
import org.threeten.bp.LocalDateTime

class WeatherConditionEntity(
        @ColumnInfo(name = "summary")
        val summary: String,

        @ColumnInfo(name = "icon")
        val icon: String,

        @ColumnInfo(name = "temp")
        val temp: Double,

        @ColumnInfo(name = "time")
        val time: LocalDateTime
)
