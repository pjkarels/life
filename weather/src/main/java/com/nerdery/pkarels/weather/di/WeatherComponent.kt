package com.bitbybitlabs.weather.di

import com.bitbybitlabs.weather.model.WeatherViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class])
interface WeatherComponent {
    fun inject(weatherViewModel: WeatherViewModel)
}
