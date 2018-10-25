package com.nerdery.pkarels.weather.module

import com.nerdery.pkarels.weather.model.WeatherViewModel
import com.nerdery.pkarels.weather.repository.WeatherRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class])
interface WeatherComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun weatherRepository(): WeatherRepository

        fun build(): WeatherComponent
    }

    fun inject(weatherViewModel: WeatherViewModel)
}
