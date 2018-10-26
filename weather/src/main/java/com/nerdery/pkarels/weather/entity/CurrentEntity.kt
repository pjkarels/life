package com.nerdery.pkarels.weather.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "current_weather")
class CurrentEntity(

        @PrimaryKey
        @ColumnInfo(name = "zip_code")
        val zipCode: Long
//
//    @ColumnInfo(name="date")
//    val date: LocalDateTime
)
