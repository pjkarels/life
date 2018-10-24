package com.nerdery.pkarels.weather.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.nerdery.pkarels.life.BaseSplitActivity
import com.nerdery.pkarels.life.Util
import com.nerdery.pkarels.life.ui.SettingsActivity
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(com.nerdery.pkarels.life.R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.nerdery.pkarels.life.R.id.action_settings) {
            val destinationIntent = Intent(this, SettingsActivity::class.java)
            Bundle().apply {
                putString(Util.INTENT_BUNDLE_PARENT_NAME, SettingsActivity::class.java.name)
                destinationIntent.putExtras(this)
            }
            startActivity(destinationIntent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
