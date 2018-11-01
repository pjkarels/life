package com.bitbybitlabs.weather.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IconService {
    companion object {
        const val URL = "https://codechallenge.nerderylabs.com"
    }

    @GET("/mobile-nat/{image}{selected}.png")
    fun getIconCall(@Path("image") image: String,
                    @Path("selected") selected: String): Call<ResponseBody>

}