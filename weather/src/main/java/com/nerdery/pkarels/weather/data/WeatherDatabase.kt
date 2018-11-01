package com.bitbybitlabs.weather.data

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.bitbybitlabs.life.converter.ThreeTenDateTimeConverters
import com.bitbybitlabs.weather.entity.CurrentConditionEntity
import com.bitbybitlabs.weather.entity.HourlyForecastsEntity

@Database(entities = [CurrentConditionEntity::class, HourlyForecastsEntity::class], version = 1)
@TypeConverters(ThreeTenDateTimeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    companion object {
        fun create(application: Application) = Room
                .databaseBuilder(application, WeatherDatabase::class.java, "weather-db")
                .build()
    }

    abstract fun weatherResponseDao(): CurrentConditionDao
}
