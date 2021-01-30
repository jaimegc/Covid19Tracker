package com.jaimegc.covid19tracker.room

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.listWorldStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStatsEntity
import com.jaimegc.covid19tracker.data.room.daos.WorldStatsDao
import com.jaimegc.covid19tracker.util.DatabaseRobolectricTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WorldStatsDaoRobolectricTest : DatabaseRobolectricTest() {

    private lateinit var worldStatsDao: WorldStatsDao

    @Before
    fun setup() {
        worldStatsDao = database.worldStatsDao()
    }

    @Test
    fun getAll_shouldReturnWorldStats() = runBlocking {
        worldStatsDao.getAll().take(1).collect {
            assertThat(it).isEqualTo(listWorldStatsEntity)
        }
    }

    @Test
    fun getAllDates_shouldReturnDates() = runBlocking {
        assertThat(worldStatsDao.getAllDates()).isEqualTo(listOf(worldStatsEntity.date))
    }
}