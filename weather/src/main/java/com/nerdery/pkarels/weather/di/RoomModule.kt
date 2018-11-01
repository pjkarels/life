package com.bitbybitlabs.weather.di

import android.app.Application
import android.arch.persistence.room.Room
import com.bitbybitlabs.weather.data.CurrentConditionDao
import com.bitbybitlabs.weather.data.WeatherDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(application: Application) {

    val weatherDatabase: WeatherDatabase = Room.databaseBuilder(application, WeatherDatabase::class.java, "weather-db").build()

    @Singleton
    @Provides
    fun providesWeatherDatabase(): WeatherDatabase {
        return weatherDatabase
    }

    @Singleton
    @Provides
    fun providesWeatherResponseDao(): CurrentConditionDao {
        return weatherDatabase.weatherResponseDao()
    }
//
//    @Singleton
//    @Provides
//    fun weatherRepository(): WeatherRepository {
//        return weatherRepository()
//    }
}