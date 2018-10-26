package com.nerdery.pkarels.weather.di

import com.nerdery.pkarels.life.LifeApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: LifeApplication) {

    @Provides
    @Singleton
    fun providesApplication(): LifeApplication {
        return application
    }
}