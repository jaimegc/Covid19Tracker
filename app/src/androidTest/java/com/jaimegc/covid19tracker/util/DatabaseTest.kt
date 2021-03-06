package com.jaimegc.covid19tracker.util

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.countryStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStatsEntity
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
abstract class DatabaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    internal lateinit var covidTrackerDao: CovidTrackerDao
    internal lateinit var database: Covid19TrackerDatabase

    @Before
    fun initDb() {
        database = getDatabase(ApplicationProvider.getApplicationContext())

        covidTrackerDao = database.covidTrackerDao()

        runBlocking {
            covidTrackerDao.populateDatabase(
                worldsStats = listOf(worldStatsEntity),
                countries = listOf(countryEntity),
                countriesStats = listOf(countryStatsEntity),
                regions = listOf(regionEntity),
                regionsStats = listOf(regionStatsEntity),
                subRegions = listOf(subRegionEntity),
                subRegionsStats = listOf(subRegionStatsEntity),
            )
        }
    }

    @After
    fun closeDb() = database.close()

    private fun getDatabase(context: Context): Covid19TrackerDatabase =
        Room.inMemoryDatabaseBuilder(context, Covid19TrackerDatabase::class.java)
            .allowMainThreadQueries()
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
}