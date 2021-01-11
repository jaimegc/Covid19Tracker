package com.jaimegc.covid19tracker.utils

import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import arrow.core.Either
import com.jaimegc.covid19tracker.ModelFactoryTest
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.home.MainActivity
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

@RunWith(AndroidJUnit4ClassRunner::class)
abstract class UITest : KoinTest, SharedPreferencesTest() {
    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var mockModule: Module
    private val covidTrackerDao: CovidTrackerDao by inject()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Before
    fun before() {
        // This is not necessary because we have the Covid19TrackerTestRunner
        // with the databaseTestModule using this. It's only an example :)
        mockModule = module(createdAtStart = true, override = true) {
            single {
                Covid19TrackerDatabase.buildTest(get())
            }
        }

        loadKoinModules(mockModule)

        mockRequests()

        runBlocking {
            covidTrackerDao.populateDatabase(
                worldsStats = listOf(ModelFactoryTest.worldStatsEntity),
                countries = listOf(ModelFactoryTest.countryEntity, ModelFactoryTest.countryEmptyRegionsEntity),
                countriesStats = listOf(ModelFactoryTest.countryStatsEntity, ModelFactoryTest.countryStatsEmptyRegionsEntity),
                regions = listOf(ModelFactoryTest.regionEntity, ModelFactoryTest.regionEmptySubRegionsEntity),
                regionsStats = listOf(ModelFactoryTest.regionStatsEntity, ModelFactoryTest.regionEmptyRegionsStatsEntity),
                subRegions = listOf(ModelFactoryTest.subRegionEntity),
                subRegionsStats = listOf(ModelFactoryTest.subRegionStatsEntity),
            )
        }

        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun after() {
        scenario.close()
        unloadKoinModules(mockModule)
    }

    private fun mockRequests() {
        // Empty requests for UpdateDatabaseWorker
        declareMock<FileUtils> {
            every { generateCurrentDates() } returns listOf()
        }

        declareMock<GetCovidTracker> {
            every { getCovidTrackerByDate(any()) } returns flow {
                emit(Either.right(stateCovidTrackerEmptyData))
            }
        }
    }
}