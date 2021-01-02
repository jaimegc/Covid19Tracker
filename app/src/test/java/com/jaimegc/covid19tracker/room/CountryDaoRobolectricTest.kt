package com.jaimegc.covid19tracker.room

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryEntity
import com.jaimegc.covid19tracker.data.room.daos.CountryDao
import com.jaimegc.covid19tracker.utils.DatabaseRobolectricTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CountryDaoRobolectricTest : DatabaseRobolectricTest() {

    private lateinit var countryDao: CountryDao

    @Before
    fun setup() {
        initDb()
        countryDao = database.countryDao()
    }

    @After
    fun close() = closeDb()

    @Test
    fun getAll_shouldReturnCountries() = runBlocking {
        countryDao.getAll().take(1).collect {
            assertThat(it).isEqualTo(listCountryEntity)
        }
    }
}