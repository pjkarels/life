package com.nerdery.pkarels.weather.ui

import android.os.Bundle
import com.nerdery.pkarels.life.BaseSplitActivity
import com.nerdery.pkarels.weather.R

class WeatherActivity : BaseSplitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, WeatherFragment.newInstance())
                    .commitNow()
        }
    }

}
