package com.nerdery.pkarels.weather.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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