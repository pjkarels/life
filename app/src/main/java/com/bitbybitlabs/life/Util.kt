package com.bitbybitlabs.life

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

private const val TEMP_THRESHOLD_FAHRENHEIT = 70.0f
private const val TEMP_THRESHOLD_CELCIUS = 40.0f

class Util {

    companion object {

        const val INTENT_BUNDLE_PARENT_NAME = "INTENT_BUNDLE_PARENT_NAME"
        const val ARGS_BUNDLE_TRANSACTION_ID = "ARGS_BUNDLE_TRANSACTION_ID"

        /**
         * "EEE, MMM d"
         */
        const val DATE_PATTERN_DAY_DATE = "EEE, MMM d"
        /**
         * "h:mm a"
         */
        const val DATE_PATTERN_HOUR_AM_PM = "h:mm a"
        /**
         * "MM/dd/yyyy"
         */
        const val DATE_PATTERN = "MM/dd/yyyy"
        /**
         * "MM/dd/yyyy HH:mm"
         */
        const val DEFAULT_DATE_ENTRY_PARSE_PATTERN = "MM/dd/yyyy HH:mm"
        /**
         * "00:01"
         */
        const val DEFAULT_TIME = "00:01"

        /**
         * "F"
         */
        const val TEMP_UNIT_ABBR_FAHRENHEIT = "F"
        /**
         * "C"
         */
        const val TEMP_UNIT_ABBR_CELCIUS = "C"

        fun provideRetrofit(client: OkHttpClient, baseUrl: String, gson: Gson): Retrofit {
            return Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

        fun provideGson(): Gson {
            val gson = GsonBuilder()
            gson.registerTypeAdapter(Date::class.java, DateDeserializer())
            return gson.create()
        }

        fun round(input: Double, spaces: Int): String {
            val inputAsString = input.toString()
            val decimalFormat = DecimalFormat()
            val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator

            val charsBeforeSeparator = inputAsString.indexOf(decimalSeparator)

            val patternAsStringBuilder = StringBuilder()
            for (i in 1..charsBeforeSeparator) {
                patternAsStringBuilder.append(decimalFormat.decimalFormatSymbols.digit)
            }
            patternAsStringBuilder.append(decimalSeparator)
            for (i in 1..spaces) {
                patternAsStringBuilder.append(decimalFormat.decimalFormatSymbols.digit)
            }

            decimalFormat.roundingMode = RoundingMode.HALF_UP
            val pattern = patternAsStringBuilder.toString()
            decimalFormat.applyPattern(pattern)

            return decimalFormat.format(input)
        }

        fun isWarmWeather(temp: Double, tempUnit: TempUnit): Boolean {
            return (tempUnit == TempUnit.FAHRENHEIT && temp >= TEMP_THRESHOLD_FAHRENHEIT
                    || tempUnit == TempUnit.CELSIUS && temp >= TEMP_THRESHOLD_CELCIUS)
        }

        fun stringToDouble(input: String): Double {
            return input.toDouble()
        }
    }
}
