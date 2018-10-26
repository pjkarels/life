package com.nerdery.pkarels.weather.di

import android.app.Application
import android.arch.persistence.room.Room
import com.nerdery.pkarels.life.data.WeatherDatabase
import com.nerdery.pkarels.life.data.WeatherResponseDao
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
    fun providesWeatherResponseDao(): WeatherResponseDao {
        return weatherDatabase.weatherResponseDao()
    }
//
//    @Singleton
//    @Provides
//    fun weatherRepository(): WeatherRepository {
//        return weatherRepository()
//    }
}