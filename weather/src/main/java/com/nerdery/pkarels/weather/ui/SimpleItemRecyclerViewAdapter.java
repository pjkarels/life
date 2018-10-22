package com.nerdery.pkarels.weather.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdery.pkarels.weather.R;
import com.nerdery.pkarels.weather.model.DayForecasts;
import com.nerdery.pkarels.weather.model.ForecastCondition;
import com.nerdery.pkarels.weather.model.WeatherViewModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private SimpleDateFormat dateFormatter;

    private final AppCompatActivity mParentActivity;
    private final WeatherViewModel viewModel;
    private final List<DayForecasts> mValues;

    SimpleItemRecyclerViewAdapter(AppCompatActivity parent,
                                  WeatherViewModel viewModel,
                                  List<DayForecasts> items) {
        dateFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        mValues = items;
        mParentActivity = parent;
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_forecase_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String dateFormat = "EEE, MMM d";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        DayForecasts forecasts = mValues.get(position);

        String title;
        if (position == 0) {
            title = mParentActivity.getString(R.string.weather_forecast_title_today);
        } else if (position == 1) {
            title = mParentActivity.getString(R.string.weather_forecast_title_tomorrow);
        } else {
            title = dateFormatter.format(forecasts.getConditions().get(0).getTime());
        }
        holder.titleView.setText(title);
        fillGrid(holder.forecastsView, forecasts.getConditions());

        holder.itemView.setTag(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void fillGrid(GridLayout forecastsView, List<ForecastCondition> hours) {
        Context context = forecastsView.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        for (ForecastCondition condition : hours) {
            View forecastView = inflater.inflate(R.layout.view_weather_cell_item, forecastsView, false);
            TextView timeView = forecastView.findViewById(R.id.weather_cell_time);
            ImageView iconView = forecastView.findViewById(R.id.weather_cell_condition);
            getIcon(condition, iconView);
            TextView tempView = forecastView.findViewById(R.id.weather_cell_temp);
            timeView.setText(dateFormatter.format(condition.getTime()));
            tempView.setText(forecastsView.getContext().getString(R.string.degrees, String.valueOf(condition.getTemp())));
            if (condition.isHightest()) {
                timeView.setTextColor(context.getResources().getColor(R.color.weather_warm));
                tempView.setTextColor(context.getResources().getColor(R.color.weather_warm));
            } else if (condition.isLowest()) {
                timeView.setTextColor(context.getResources().getColor(R.color.weather_cool));
                tempView.setTextColor(context.getResources().getColor(R.color.weather_cool));
            }
            forecastsView.addView(forecastView);
        }
    }

    private void getIcon(ForecastCondition condition, final ImageView iconView) {
        iconView.setImageBitmap(viewModel.getIcon(condition.getIcon(), condition.isHightest() || condition.isLowest()).getImage());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView;
        GridLayout forecastsView;

        ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.weather_card_title);
            forecastsView = view.findViewById(R.id.weather_card_forecasts);
        }
    }
}
