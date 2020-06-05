package com.jaimegc.covid19tracker.data.preference

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

abstract class BasePreferences(context: Context) {
    protected var settings: SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
}

class CovidTrackerPreferences(
    context: Context
) : BasePreferences(context) {

    companion object {
        private const val KEY_COVID_TRACKER_LAST_UPDATE_TIME = "KEY_COUNTRY_LAST_UPDATE_TIME"
        private const val KEY_COUNTRY_LAST_UPDATE_DEFAULT = 0L
        private val CACHE_TIME = TimeUnit.HOURS.toMillis(1)
    }

    fun saveTime() =
        settings.edit().putLong(KEY_COVID_TRACKER_LAST_UPDATE_TIME, System.currentTimeMillis()).apply()

    fun isCacheExpired(): Boolean =
        System.currentTimeMillis() - getLastUpdateTime() > CACHE_TIME

    private fun getLastUpdateTime(): Long =
        settings.getLong(KEY_COVID_TRACKER_LAST_UPDATE_TIME, KEY_COUNTRY_LAST_UPDATE_DEFAULT)
}

class CountryPreferences(
    context: Context
) : BasePreferences(context) {

    companion object {
        private const val KEY_COUNTRY = "KEY_COUNTRY"
        private const val KEY_COUNTRY_DEFAULT = "spain"
    }

    fun save(countryId: String) =
        settings.edit().putString(KEY_COUNTRY, countryId).apply()

    fun getId(): String =
        settings.getString(KEY_COUNTRY, KEY_COUNTRY_DEFAULT)!!
}