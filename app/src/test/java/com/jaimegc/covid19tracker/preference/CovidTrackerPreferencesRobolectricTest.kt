package com.jaimegc.covid19tracker.preference

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.util.SharedPreferencesRobolectricTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CovidTrackerPreferencesRobolectricTest : SharedPreferencesRobolectricTest() {

    private lateinit var covidTrackerPreferences: CovidTrackerPreferences

    @Before
    fun setUp() {
        clearPrefs()
        covidTrackerPreferences = CovidTrackerPreferences(context)
    }

    @Test
    fun isCacheExpiredFirsTime_shouldReturnTrue() {
        assertThat(covidTrackerPreferences.isCacheExpired()).isTrue()
    }

    @Test
    fun saveTime_shouldReturnIsCacheExpiredFalse() {
        assertThat(covidTrackerPreferences.isCacheExpired()).isTrue()
        covidTrackerPreferences.saveTime()
        assertThat(covidTrackerPreferences.isCacheExpired()).isFalse()
    }
}
