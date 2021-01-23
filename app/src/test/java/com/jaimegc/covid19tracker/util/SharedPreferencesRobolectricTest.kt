package com.jaimegc.covid19tracker.util

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
abstract class SharedPreferencesRobolectricTest : AutoCloseKoinTest() {

    companion object {
        private const val PREFS_NAME = "prefs"
    }

    internal val context: Context = InstrumentationRegistry.getInstrumentation().context

    @Before
    fun clearPrefs() {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit()
    }
}