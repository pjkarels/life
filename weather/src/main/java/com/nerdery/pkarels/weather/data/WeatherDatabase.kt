package com.nerdery.pkarels.weather.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import com.nerdery.pkarels.weather.model.WeatherResponse

@Database(entities = arrayOf(WeatherResponse::class), version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherResponseDao(): WeatherResponseDao
}
