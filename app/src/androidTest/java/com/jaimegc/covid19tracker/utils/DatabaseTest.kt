package com.jaimegc.covid19tracker.utils

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jaimegc.covid19tracker.ModelFactoryTest
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
open class DatabaseTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var covidTrackerDao: CovidTrackerDao
    internal lateinit var database: Covid19TrackerDatabase

    @Before
    fun initDb() {
        database = getDatabase(ApplicationProvider.getApplicationContext())

        covidTrackerDao = database.covidTrackerDao()

        runBlocking {
            covidTrackerDao.populateDatabase(
                worldsStats = listOf(ModelFactoryTest.worldStatsEntity),
                countries = listOf(ModelFactoryTest.countryEntity),
                countriesStats = listOf(ModelFactoryTest.countryStatsEntity),
                regions = listOf(ModelFactoryTest.regionEntity),
                regionsStats = listOf(ModelFactoryTest.regionStatsEntity),
                subRegions = listOf(ModelFactoryTest.subRegionEntity),
                subRegionsStats = listOf(ModelFactoryTest.subRegionStatsEntity),
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