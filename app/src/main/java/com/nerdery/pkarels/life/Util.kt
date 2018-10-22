package com.nerdery.pkarels.life

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Util {

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
}
