package com.nerdery.pkarels.weather.data

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.nerdery.pkarels.life.converter.ThreeTenDateTimeConverters
import com.nerdery.pkarels.weather.entity.CurrentEntity

@Database(entities = [CurrentEntity::class], version = 1)
@TypeConverters(ThreeTenDateTimeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    companion object {
        fun create(application: Application) = Room
                .databaseBuilder(application, WeatherDatabase::class.java, "weather-db")
                .build()
    }

    abstract fun weatherResponseDao(): WeatherResponseDao
}
