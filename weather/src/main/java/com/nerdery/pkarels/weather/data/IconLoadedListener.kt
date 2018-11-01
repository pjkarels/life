package com.bitbybitlabs.weather.data

import android.graphics.Bitmap

interface IconLoadedListener {
    fun onIconLoaded(image: Bitmap)
    fun onIconLoadedError(message: String)
}