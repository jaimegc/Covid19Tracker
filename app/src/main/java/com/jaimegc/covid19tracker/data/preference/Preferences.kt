package com.jaimegc.covid19tracker.data.preference

import android.content.Context
import android.content.SharedPreferences

abstract class BasePreferences(context: Context) {
    protected var settings: SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
}

class CountryPreferences(
    context: Context
) : BasePreferences(context) {

    companion object {
        private const val KEY_COUNTRY_PREFERENCES = "KEY_AIRPORT_LAST_UPDATED_TIME"
        private const val KEY_COUNTRY_DEFAULT_PREFERENCES = "spain"
    }

    fun save(countryId: String) =
        settings.edit().putString(KEY_COUNTRY_PREFERENCES, countryId).apply()

    fun getId(): String =
        settings.getString(KEY_COUNTRY_PREFERENCES, KEY_COUNTRY_DEFAULT_PREFERENCES)!!
}