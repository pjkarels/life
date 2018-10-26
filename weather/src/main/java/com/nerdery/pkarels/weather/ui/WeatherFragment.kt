package com.nerdery.pkarels.weather.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdery.pkarels.life.LifeApplication
import com.nerdery.pkarels.life.TempUnit
import com.nerdery.pkarels.life.ZipCodeService
import com.nerdery.pkarels.life.ui.SettingsActivity
import com.nerdery.pkarels.weather.R
import com.nerdery.pkarels.weather.model.WeatherViewModel

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
        val context = activity as AppCompatActivity
        ZipCodeService.getLatLongByZip(context.applicationContext, zipPref, this)
    }

    override fun onLocationFound(location: ZipCodeService.ZipLocation) {
        val activity = this.activity as AppCompatActivity
        activity.supportActionBar?.title = getString(com.nerdery.pkarels.life.R.string.location_display, location.city, location.state)
        val units = sharedPreferences.getString("pref_title_units", getString(com.nerdery.pkarels.life.R.string.pref_units_default))
        val tempUnit = if (units == getString(com.nerdery.pkarels.life.R.string.pref_units_default)) TempUnit.FAHRENHEIT else TempUnit.CELSIUS
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        viewModel.init(location, tempUnit, activity.application as LifeApplication)
        viewModel.weatherResponseContainer.observe(this, Observer { t ->
            //            t as CurrentEntity?
            val summaryView = view?.findViewById<View>(R.id.weather_summary)
//            if (summaryView != null) {
//                if (Util.isWarmWeather(t.currentForecast.temp, tempUnit)) {
//                    activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(
//                            activity.resources.getColor(R.color.weather_warm)
//                    ))
//                    summaryView.setBackgroundColor(activity.resources.getColor(R.color.weather_warm))
//                } else {
//                    activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(
//                            activity.resources.getColor(R.color.weather_cool)
//                    ))
//                    summaryView.setBackgroundColor(activity.resources.getColor(R.color.weather_cool))
//                }
//            }
//            val conditionView = view?.findViewById<TextView>(R.id.weather_summary_conditions)
//            conditionView?.text = t.currentForecast.summary
//            val tempView = view?.findViewById<TextView>(R.id.weather_summary_temp)
//            tempView?.text = activity.getString(R.string.degrees, t.currentForecast.getTemperature(),
//                    if (tempUnit == TempUnit.FAHRENHEIT) {
//                        Util.TEMP_UNIT_ABBR_FAHRENHEIT
//                    } else {
//                        Util.TEMP_UNIT_ABBR_CELCIUS
//                    })
//
//            val recyclerView = view?.findViewById<RecyclerView>(R.id.weather_recyclerView)
//            recyclerView?.adapter = SimpleItemRecyclerViewAdapter(activity, viewModel, t.forecasts)
        })
    }

    override fun onLocationNotFound() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
