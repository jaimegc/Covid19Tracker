package com.jaimegc.covid19tracker.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.country
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionEntity
import com.jaimegc.covid19tracker.data.room.daos.RegionDao
import com.jaimegc.covid19tracker.utils.DatabaseTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegionDaoTest : DatabaseTest() {

    private lateinit var regionDao: RegionDao

    @Before
    fun setup() {
        regionDao = database.regionDao()
    }

    @Test
    fun getAll_shouldReturnRegions() = runBlocking {
        regionDao.getAll().take(1).collect {
            assertThat(it).isEqualTo(listRegionEntity)
        }
    }

    @Test
    fun getByCountry_shouldReturnRegionsByCountry() = runBlocking {
        regionDao.getByCountry(
            country.id
        ).take(1).collect {
            assertThat(it).isEqualTo(listRegionEntity)
        }
    }
}