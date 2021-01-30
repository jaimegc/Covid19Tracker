package com.jaimegc.covid19tracker.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import arrow.core.Either
import com.jaimegc.covid19tracker.ModelFactoryTest.country
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEmptyRegions
import com.jaimegc.covid19tracker.ModelFactoryTest.region
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEmptySubRegions
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostConfirmedSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostDeathsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostOpenCasesSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostRecoveredSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryOnlyStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryOnlyStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountrySpainAndorraSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndorraSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionOnlyStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionOnlyStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionSpainSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListWorldStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListWorldStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListSubRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListSubRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsSuccess
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.domain.usecase.GetRegion
import com.jaimegc.covid19tracker.domain.usecase.GetRegionStats
import com.jaimegc.covid19tracker.domain.usecase.GetSubRegionStats
import com.jaimegc.covid19tracker.domain.usecase.GetWorldAndCountries
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

@RunWith(AndroidJUnit4ClassRunner::class)
abstract class UITest : KoinTest, SharedPreferencesTest() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockModule: Module
    private val getWorldAndCountries = mockk<GetWorldAndCountries>()
    private val getWorldStats = mockk<GetWorldStats>()
    private val getCountryStats = mockk<GetCountryStats>()
    private val getCountry = mockk<GetCountry>()
    private val getRegion = mockk<GetRegion>()
    private val getRegionStats = mockk<GetRegionStats>()
    private val getSubRegionStats = mockk<GetSubRegionStats>()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Before
    fun before() {
        mockModule = module(createdAtStart = true, override = true) {
            single {
                getWorldAndCountries
            }
            single {
                getWorldStats
            }
            single {
                getCountryStats
            }
            single {
                getCountry
            }
            single {
                getRegion
            }
            single {
                getRegionStats
            }
            single {
                getSubRegionStats
            }
        }

        loadKoinModules(mockModule)

        mockRequests()
    }

    @After
    fun after() {
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

        every { getWorldAndCountries.getWorldAndCountriesByDate(any()) } returns flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { getWorldStats.getWorldAllStats() } returns flow {
            Either.right(stateListWorldStatsLoading)
            emit(Either.right(stateListWorldStatsSuccess))
        }

        every { getCountryStats.getCountriesStatsOrderByConfirmed() } returns flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsSuccess))
        }

        every { getCountryStats.getCountriesAndStatsWithMostConfirmed() } returns flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostConfirmedSuccess))
        }

        every { getCountryStats.getCountriesAndStatsWithMostDeaths() } returns flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostDeathsSuccess))
        }

        every { getCountryStats.getCountriesAndStatsWithMostOpenCases() } returns flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostOpenCasesSuccess))
        }

        every { getCountryStats.getCountriesAndStatsWithMostRecovered() } returns flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostRecoveredSuccess))
        }

        every { getCountry.getCountries() } returns flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountrySpainAndorraSuccess))
        }

        every { getRegion.getRegionsByCountry(country.id) } returns flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionSpainSuccess))
        }

        every { getRegion.getRegionsByCountry(countryEmptyRegions.id) } returns flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionAndorraSuccess))
        }

        every { getRegionStats.getRegionsAndStatsWithMostConfirmed(country.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
        }

        every { getRegionStats.getRegionsAndStatsWithMostDeaths(country.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostDeathsListRegionAndStatsSuccess))
        }

        every { getRegionStats.getRegionsAndStatsWithMostRecovered(country.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostRecoveredListRegionAndStatsSuccess))
        }

        every { getRegionStats.getRegionsAndStatsWithMostOpenCases(country.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostOpenCasesListRegionAndStatsSuccess))
        }

        every { getRegionStats.getRegionsAndStatsWithMostConfirmed(countryEmptyRegions.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
        }

        every { getRegionStats.getRegionsAndStatsWithMostDeaths(countryEmptyRegions.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
        }

        every { getRegionStats.getRegionsAndStatsWithMostRecovered(countryEmptyRegions.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
        }

        every { getRegionStats.getRegionsAndStatsWithMostOpenCases(countryEmptyRegions.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
        }

        every { getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(country.id, region.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
        }

        every { getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(country.id, region.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostDeathsListSubRegionAndStatsSuccess))
        }

        every { getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(country.id, region.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostRecoveredListSubRegionAndStatsSuccess))
        }

        every { getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(country.id, region.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostOpenCasesListSubRegionAndStatsSuccess))
        }

        every { getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(country.id, regionEmptySubRegions.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
        }

        every { getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(country.id, regionEmptySubRegions.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
        }

        every { getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(country.id, regionEmptySubRegions.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
        }

        every { getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(country.id, regionEmptySubRegions.id) } returns flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
        }

        every { getCountryStats.getCountryAndStatsByDate(country.id, any()) } returns flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsSuccess))
        }

        every { getRegionStats.getRegionsStatsOrderByConfirmed(country.id, any()) } returns flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsSuccess))
        }

        every { getCountryStats.getCountryAndStatsByDate(countryEmptyRegions.id, any()) } returns flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsEmptySuccess))
        }

        every { getRegionStats.getRegionsStatsOrderByConfirmed(countryEmptyRegions.id, any()) } returns flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsEmptyData))
        }
        
        every { getRegionStats.getRegionAndStatsByDate(country.id, any(), any()) } returns flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsSuccess))
        }
        
        every { getSubRegionStats.getSubRegionsStatsOrderByConfirmed(country.id, region.id, any()) } returns flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsSuccess))
        }
        
        every { getSubRegionStats.getSubRegionsStatsOrderByConfirmed(country.id, regionEmptySubRegions.id, any()) } returns flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsEmptyData))
        }

        every { getCountryStats.getCountryAllStats(any()) } returns flow {
            emit(Either.right(stateListCountryOnlyStatsLoading))
            emit(Either.right(stateListCountryOnlyStatsSuccess))
        }

        every { getRegionStats.getRegionsAllStatsOrderByConfirmed(country.id) } returns flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.right(stateListRegionAndStatsSuccess))
        }

        every { getRegionStats.getRegionsAllStatsOrderByConfirmed(countryEmptyRegions.id) } returns flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.right(stateListRegionAndStatsEmptyData))
        }

        every { getRegionStats.getRegionAllStats(country.id, any()) } returns flow {
            emit(Either.right(stateListRegionOnlyStatsLoading))
            emit(Either.right(stateListRegionOnlyStatsSuccess))
        }

        every { getSubRegionStats.getSubRegionsAllStatsOrderByConfirmed(country.id, region.id) } returns flow {
            emit(Either.right(stateListSubRegionAndStatsLoading))
            emit(Either.right(stateListSubRegionAndStatsSuccess))
        }

        every { getSubRegionStats.getSubRegionsAllStatsOrderByConfirmed(country.id, regionEmptySubRegions.id) } returns flow {
            emit(Either.right(stateListSubRegionAndStatsLoading))
            emit(Either.right(stateListSubRegionAndStatsEmptyData))
        }
    }
}