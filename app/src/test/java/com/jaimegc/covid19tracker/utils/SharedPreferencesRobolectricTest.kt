package com.jaimegc.covid19tracker.utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class SharedPreferencesRobolectricTest : KoinTest {

    companion object {
        private const val PREFS_NAME = "prefs"
    }

    internal val context: Context = InstrumentationRegistry.getInstrumentation().context

    @Before
    fun clearPrefs() {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit()
    }

    @After
    fun closeDb() = stopKoin()
}