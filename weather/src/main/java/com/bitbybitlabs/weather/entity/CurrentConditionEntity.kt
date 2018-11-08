package com.bitbybitlabs.weather.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "current_condition")
data class CurrentConditionEntity(

        @PrimaryKey
        @ColumnInfo(name = "zipcode")
        val zipcode: Long,

        @ColumnInfo(name = "date")
        val date: LocalDateTime,

        @Embedded
        val weatherCondition: WeatherConditionEntity
)