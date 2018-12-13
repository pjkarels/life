package com.bitbybitlabs.life.installed

import com.bitbybitlabs.life.ApiServicesProvider
import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.jakewharton.threetenabp.AndroidThreeTen

class LifeApplication : SplitCompatApplication() {
    lateinit var apiServicesProvider: ApiServicesProvider

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        apiServicesProvider = ApiServicesProvider(this)
    }
}