package com.bitbybitlabs.weather.di

import com.bitbybitlabs.life.LifeApplication
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