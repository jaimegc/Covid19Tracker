package com.jaimegc.covid19tracker.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jaimegc.covid19tracker.data.preference.CountryPreferences
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class SharedPreferencesTest {

    companion object {
        private const val PREFS_NAME = "prefs"
        internal const val COUNTRY_DEFAULT = "spain"
    }

    internal val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun initPrefs() {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit()
        CountryPreferences(context).save(COUNTRY_DEFAULT)
    }
}