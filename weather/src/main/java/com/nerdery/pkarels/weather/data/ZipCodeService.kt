package com.nerdery.pkarels.weather.data

import android.content.Context

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.nerdery.pkarels.life.R

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


private const val BUFFER_SIZE = 1024
/**
 * This Service is used to lookup location details of a specific US ZIP code
 */
class ZipCodeService {

    interface ZipLocationListener {
        fun onLocationFound(location: ZipLocation)
        fun onLocationNotFound()
    }

    inner class ZipLocation {
        @SerializedName("z")
        val zipCode: Long = 0
        @SerializedName("c")
        val city: String? = null
        @SerializedName("s")
        val state: String? = null
        @SerializedName("la")
        val latitude: Double = 0.toDouble()
        @SerializedName("lo")
        val longitude: Double = 0.toDouble()
    }

    companion object {

        /**
         * Request location details of a given zip code
         * @param context Generic context to retrieve zip codes resources
         * @param zipCode Numerical zip code
         * @param listener ZipLocationListener used to listen for successful result or error
         */
        fun getLatLongByZip(context: Context, zipCode: String, listener: ZipLocationListener) {
            var zipLong: Long = 0
            try {
                zipLong = java.lang.Long.valueOf(zipCode)
            } catch (e: NumberFormatException) {
                listener.onLocationNotFound()
            }

            val stream = context.resources.openRawResource(R.raw.zipcodes)
            val jsonString = readJsonFile(stream)
            val gson = Gson()
            val locations = gson.fromJson(jsonString, Array<ZipLocation>::class.java)
            var isFound = false
            lateinit var foundLocation: ZipLocation
            for (location in locations) {
                if (location.zipCode == zipLong) {
                    isFound = true
                    foundLocation = location
                    break // no reason to keep looping
                }
            }
            if (isFound) {
                listener.onLocationFound(foundLocation)
            } else {
                listener.onLocationNotFound()
            }
        }

        private fun readJsonFile(inputStream: InputStream): String? {
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(BUFFER_SIZE)
            val length = inputStream.read(buffer)
            try {
                while (length != -1) {
                    outputStream.write(buffer, 0, length)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

            return outputStream.toString()
        }
    }

}
