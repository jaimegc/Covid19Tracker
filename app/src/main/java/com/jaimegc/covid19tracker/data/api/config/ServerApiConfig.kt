package com.jaimegc.covid19tracker.data.api.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jaimegc.covid19tracker.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


open class ServerApiConfig(
    private val baseUrl: String,
    private val gsonConverter: Gson? = null
) {

    companion object {
        private const val TIME_OUT_SECONDS: Long = 60
    }

    var retrofit: Retrofit

    init {
        retrofit = initialize()
    }

    private fun initialize(): Retrofit {

        lateinit var retrofit: Retrofit

        val gsonConverterFactory = gsonConverter?.let {
            GsonConverterFactory.create(gsonConverter)
        } ?: GsonConverterFactory.create()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient().build())
            .addConverterFactory(gsonConverterFactory)
            .build()

        return retrofit
    }

    private fun defaultConverter(): Gson = GsonBuilder().create()

    private fun getOkHttpClient(): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()

        client.connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
        client.readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        client.addInterceptor(logging)

        return client
    }
}

class ServerApiCovidTrackerConfig(
    baseUrl: String
) : ServerApiConfig(baseUrl)

class ServerApiCovidTrackerConfigBuilder(
    private val baseUrl: String = BuildConfig.BASE_URL
) {
    fun build(): ServerApiCovidTrackerConfig = ServerApiCovidTrackerConfig(baseUrl)
}