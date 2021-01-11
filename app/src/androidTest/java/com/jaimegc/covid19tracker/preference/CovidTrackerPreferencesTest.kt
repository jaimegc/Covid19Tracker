package com.jaimegc.covid19tracker.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.utils.SharedPreferencesTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CovidTrackerPreferencesTest : SharedPreferencesTest() {

    private lateinit var covidTrackerPreferences: CovidTrackerPreferences

    @Before
    fun setUp() {
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
