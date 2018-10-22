package com.nerdery.pkarels.weather.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nerdery.pkarels.life.LifeApplication
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.life.ui.SettingsActivity
import com.nerdery.pkarels.weather.R
import com.nerdery.pkarels.weather.model.TempUnit
import com.nerdery.pkarels.weather.model.WeatherResponse
import com.nerdery.pkarels.weather.model.WeatherViewModel
import com.nerdery.pkarels.weather.repository.WeatherRepository

class WeatherFragment : Fragment(), ZipCodeService.ZipLocationListener {
    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var viewModel: WeatherViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val zipPref = sharedPreferences.getString("pref_title_zip", "")
        if (zipPref == "") {
            if (zipPref == "") {
                startActivity(Intent(activity, SettingsActivity::class.java))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()

        val zipPref = sharedPreferences.getString("pref_title_zip", "")
        val context = activity?.applicationContext
        if (context != null) {
            ZipCodeService.getLatLongByZip(context.applicationContext, zipPref, this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onLocationFound(location: ZipCodeService.ZipLocation) {
        val units = sharedPreferences.getString("pref_title_units", getString(com.nerdery.pkarels.life.R.string.pref_units_default))
        val tempUnit = if (units == getString(com.nerdery.pkarels.life.R.string.pref_units_default)) TempUnit.FAHRENHEIT else TempUnit.CELSIUS
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        viewModel.init(WeatherRepository(activity?.application as LifeApplication), location, tempUnit)
        viewModel.weatherResponse.observe(this, Observer { t ->
            t as WeatherResponse
            val summaryView = view?.findViewById<TextView>(R.id.weather_summary_conditions)
            summaryView?.text = t.currentForecast.summary
            val tempView = view?.findViewById<TextView>(R.id.weather_summary_temp)
            tempView?.text = t.currentForecast.getTemp()
        })
    }

    override fun onLocationNotFound() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
