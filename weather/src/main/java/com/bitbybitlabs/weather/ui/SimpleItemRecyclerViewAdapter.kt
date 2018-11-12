package com.bitbybitlabs.weather.ui

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bitbybitlabs.life.TempUnit
import com.bitbybitlabs.life.Util
import com.bitbybitlabs.weather.R
import com.bitbybitlabs.weather.data.IconLoadedListener
import com.bitbybitlabs.weather.model.DayForecasts
import com.bitbybitlabs.weather.model.ForecastCondition
import com.bitbybitlabs.weather.model.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class SimpleItemRecyclerViewAdapter internal constructor(private val mParentActivity: AppCompatActivity,
                                                         private val viewModel: WeatherViewModel,
                                                         private val mValues: List<DayForecasts>) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_forecase_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFormatter = SimpleDateFormat(Util.DATE_PATTERN_DAY_DATE, Locale.getDefault())
        val forecasts = mValues[position]

        val title: String
        if (position == 0) {
            title = mParentActivity.getString(R.string.weather_forecast_title_today)
        } else if (position == 1) {
            title = mParentActivity.getString(R.string.weather_forecast_title_tomorrow)
        } else {
            title = dateFormatter.format(forecasts.conditions[0].getTimeAsDate())
        }
        holder.titleView.text = title
        fillGrid(holder.forecastsView, forecasts.conditions)

        holder.itemView.tag = mValues[position]
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    private fun fillGrid(forecastsView: GridLayout, hours: List<ForecastCondition>) {
        val dateFormatter = SimpleDateFormat(Util.DATE_PATTERN_HOUR_AM_PM, Locale.getDefault())

        val context = forecastsView.context
        val inflater = LayoutInflater.from(context)
        for (condition in hours) {
            val forecastView = inflater.inflate(R.layout.view_weather_cell_item, forecastsView, false)
            val timeView = forecastView.findViewById<TextView>(R.id.weather_cell_time)
            val iconView = forecastView.findViewById<ImageView>(R.id.weather_cell_condition)
            val conditionDescriptionView = forecastView.findViewById<TextView>(R.id.weather_cell_condition_text)
            val tempView = forecastView.findViewById<TextView>(R.id.weather_cell_temp)

            getIcon(condition, iconView, conditionDescriptionView)
            iconView.setColorFilter(ContextCompat.getColor(context,
                    if (condition.isHighest) R.color.weather_warm
                    else R.color.weather_cool),
                    PorterDuff.Mode.SRC_ATOP)

            timeView.text = dateFormatter.format(condition.getTimeAsDate())
            tempView.text = forecastsView.context.getString(R.string.degrees, condition.getTemperature(),
                    if (condition.tempUnit == TempUnit.FAHRENHEIT) {
                        Util.TEMP_UNIT_ABBR_FAHRENHEIT
                    } else {
                        Util.TEMP_UNIT_ABBR_CELCIUS
                    })
            if (condition.isHighest) {
                timeView.setTextColor(context.resources.getColor(R.color.weather_warm))
                tempView.setTextColor(context.resources.getColor(R.color.weather_warm))
            } else if (condition.isLowest) {
                timeView.setTextColor(context.resources.getColor(R.color.weather_cool))
                tempView.setTextColor(context.resources.getColor(R.color.weather_cool))
            }
            conditionDescriptionView.text = condition.summary
            forecastsView.addView(forecastView)
        }
    }

    private fun getIcon(condition: ForecastCondition, iconView: ImageView, conditionView: TextView) {
        viewModel.getIcon(condition.icon, condition.isHighest || condition.isLowest, object : IconLoadedListener {

            override fun onIconLoaded(image: Bitmap) {
                iconView.setImageBitmap(image)
                iconView.visibility = View.VISIBLE
                conditionView.visibility = View.GONE
            }

            override fun onIconLoadedError(message: String) {
                conditionView.visibility = View.VISIBLE
                iconView.visibility = View.GONE
            }
        })
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.weather_card_title)
        val forecastsView: GridLayout = view.findViewById(R.id.weather_card_forecasts)
    }
}