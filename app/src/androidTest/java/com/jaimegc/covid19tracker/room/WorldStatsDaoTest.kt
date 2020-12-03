package com.jaimegc.covid19tracker.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStatsEntity
import com.jaimegc.covid19tracker.data.room.daos.WorldStatsDao
import com.jaimegc.covid19tracker.utils.DatabaseTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorldStatsDaoTest : DatabaseTest() {

    private lateinit var worldStatsDao: WorldStatsDao

    @Before
    fun setup() {
        initDb()
        worldStatsDao = database.worldStatsDao()
    }

    @After
    fun close() = closeDb()

    @Test
    fun getAll_shouldReturnWorldStats() = runBlocking {
        worldStatsDao.getAll().take(1).collect {
            assertThat(it).isEqualTo(listOf(worldStatsEntity))
        }
    }

    @Test
    fun getAllDates_shouldReturnDates() = runBlocking {
        assertThat(worldStatsDao.getAllDates()).isEqualTo(listOf(worldStatsEntity.date))
    }
}