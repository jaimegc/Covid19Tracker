package com.jaimegc.covid19tracker.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryEntity
import com.jaimegc.covid19tracker.data.room.daos.CountryDao
import com.jaimegc.covid19tracker.util.DatabaseTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class CountryDaoTest : DatabaseTest() {

    private lateinit var countryDao: CountryDao

    @Before
    fun setup() {
        countryDao = database.countryDao()
    }

    @Test
    fun getAll_shouldReturnCountries() = runBlocking {
        countryDao.getAll().take(1).collect {
            assertThat(it).isEqualTo(listCountryEntity)
        }
    }
}