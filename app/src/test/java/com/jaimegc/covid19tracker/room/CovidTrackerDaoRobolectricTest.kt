package com.jaimegc.covid19tracker.room

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.worldAndCountriesStatsPojo
import com.jaimegc.covid19tracker.utils.DatabaseRobolectricTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CovidTrackerDaoRobolectricTest : DatabaseRobolectricTest() {

    @Before
    fun setup() {
        covidTrackerDao = database.covidTrackerDao()
    }

    @Test
    fun getWorldAndCountriesStatsByLastDate_shouldReturnPojo() = runBlocking {
        covidTrackerDao.getWorldAndCountriesStatsByLastDate().take(1).collect {
            assertThat(it).isEqualTo(worldAndCountriesStatsPojo)
        }
    }

    @Test
    fun getWorldAndCountriesStatsByDate_shouldReturnPojo() = runBlocking {
        covidTrackerDao.getWorldAndCountriesStatsByDate(
            worldAndCountriesStatsPojo.worldStats!!.dateTimestamp
        ).take(1).collect {
                assertThat(it).isEqualTo(worldAndCountriesStatsPojo)
        }
    }
}