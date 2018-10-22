package com.nerdery.pkarels.life

import android.app.Application
import com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES
import com.jakewharton.picasso.OkHttp3Downloader
import com.nerdery.pkarels.life.api.IconApi
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File

/**
 * Provides [Picasso]
 * that are all ready setup and ready to use.
 */
open class ApiServicesProvider
/**
 * Constructor.
 *
 * @param application application context used for creating network caches.
 */
(application: Application) {

    val picasso: Picasso
    val client: OkHttpClient
    val iconApi: IconApi

    init {
        client = createOkHttpClient(application)
        iconApi = IconApi()
        picasso = Picasso.Builder(application)
                .downloader(OkHttp3Downloader(client))
                .listener { picasso, uri, e -> Timber.e("Failed to load image: %s", uri) }
                .build()
    }

    private fun createOkHttpClient(app: Application): OkHttpClient {
        // Install an HTTP cache in the application cache directory.
        val cacheDir = File(app.cacheDir, "http")
        val cache = Cache(cacheDir, DISK_CACHE_SIZE.toLong())

        return OkHttpClient.Builder()
                .cache(cache)
                .build()
    }

    companion object {
        private val DISK_CACHE_SIZE = MEGABYTES.toBytes(50).toInt()
    }
}
