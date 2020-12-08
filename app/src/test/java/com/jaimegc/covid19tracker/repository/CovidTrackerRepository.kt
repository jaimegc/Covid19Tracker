package com.jaimegc.covid19tracker.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.countryOneStats
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTracker
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountry
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryOnlyStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegion
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionOnlyStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listWorldStats
import com.jaimegc.covid19tracker.ModelFactoryTest.regionOneStats
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.utils.MainCoroutineRule
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCountryOneStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCountryOneStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCovidTrackerLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCovidTrackerSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostConfirmedListRegionAndStats
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostConfirmedListRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostConfirmedListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostConfirmedListSubRegionAndStats
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostConfirmedListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryOnlyStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryOnlyStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountrySuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionOnlyStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListSubRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListSubRegionStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListSubRegionStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListWorldStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListWorldStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateRegionOneStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateRegionOneStatsSuccess
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CovidTrackerRepository {

    companion object {
        private val DATES = listOf("date1", "date2", "date3")
        private const val DATE = "date"
        private const val ID_COUNTRY = "id_country"
        private const val ID_REGION = "id_region"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineRule()

    @MockK
    private lateinit var local: LocalCovidTrackerDatasource
    @MockK
    private lateinit var remote: RemoteCovidTrackerDatasource
    @MockK
    private lateinit var preferences: CovidTrackerPreferences

    private lateinit var repository: CovidTrackerRepository

    @Before
    fun init() {
        MockKAnnotations.init(this)
        repository = CovidTrackerRepository(local, remote, preferences)
    }

    @Test
    fun `get covid tracker by date should return loading and save database if the cache is expired`() = runBlocking {
        every { preferences.isCacheExpired() } returns true
        coEvery { remote.getCovidTrackerByDate(any()) } returns Either.right(covidTracker)
        coEvery { local.save(covidTracker) } returns Unit

        val flowRepository = repository.getCovidTrackerByDate(DATE)

        verify { preferences.isCacheExpired() }
        verify { local.getCovidTrackerByDate(any()) wasNot Called }

        flowRepository.collect { data ->
            assertThatIsEqualToState(data, stateCovidTrackerLoading)
        }
        coVerify { remote.getCovidTrackerByDate(any()) }
        coVerify { local.save(covidTracker) }
    }

    @Test
    fun `get covid tracker by date should return loading and error if the cache is expired and connection is offline`() = runBlocking {
        every { preferences.isCacheExpired() } returns true
        coEvery { remote.getCovidTrackerByDate(any()) } returns Either.left(DomainError.NoInternetDomainError)

        val flowRepository = repository.getCovidTrackerByDate(DATE)

        verify { preferences.isCacheExpired() }
        verify { local.getCovidTrackerByDate(any()) wasNot Called }
        coVerify(exactly = 0) { local.save(covidTracker) }
        flowRepository.collect { data ->
            assertThatIsEqualToState(data, stateCovidTrackerLoading)
        }
    }

    @Test
    fun `get covid tracker by date should return only loading if the cache is not expired`() = runBlocking {
        every { preferences.isCacheExpired() } returns false

        val flowRepository = repository.getCovidTrackerByDate(DATE)

        verify { preferences.isCacheExpired() }
        verify { local.getCovidTrackerByDate(any()) wasNot Called }
        coVerify { remote.getCovidTrackerByDate(any()) wasNot Called }
        flowRepository.collect { data ->
            assertThatIsEqualToState(data, stateCovidTrackerLoading)
        }
    }

    @Test
    fun `get world and countries by date should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(covidTracker))
        }

        every { local.getWorldAndCountriesByDate(any()) } returns flow

        val flowRepository = repository.getWorldAndCountriesByDate(DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerSuccess))
            }
        }
        verify { local.getWorldAndCountriesByDate(any()) }
    }

    @Test
    fun `get world and countries by date with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getWorldAndCountriesByDate(any()) } returns flow

        val flowRepository = repository.getWorldAndCountriesByDate(DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateCovidTrackerEmptyData)
            }
        }
        verify { local.getWorldAndCountriesByDate(any()) }
    }

    @Test
    fun `get world all stats should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listWorldStats))
        }

        every { local.getWorldAllStats() } returns flow

        val flowRepository = repository.getWorldAllStats()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListWorldStatsSuccess))
            }
        }
        verify { local.getWorldAllStats() }
    }

    @Test
    fun `get world all stats with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getWorldAllStats() } returns flow

        val flowRepository = repository.getWorldAllStats()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListWorldStatsEmptyData)
            }
        }
        verify { local.getWorldAllStats() }
    }

    @Test
    fun `get country all stats should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listCountryOnlyStats))
        }

        every { local.getCountryAllStats(any()) } returns flow

        val flowRepository = repository.getCountryAllStats(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryOnlyStatsSuccess))
            }
        }
        verify { local.getCountryAllStats(any()) }
    }

    @Test
    fun `get country all stats with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getCountryAllStats(any()) } returns flow

        val flowRepository = repository.getCountryAllStats(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListCountryOnlyStatsEmptyData)
            }
        }
        verify { local.getCountryAllStats(any()) }
    }

    @Test
    fun `get region all stats should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listRegionOnlyStats))
        }

        every { local.getRegionAllStats(any(), any()) } returns flow

        val flowRepository = repository.getRegionAllStats(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionOnlyStatsSuccess))
            }
        }
        verify { local.getRegionAllStats(any(), any()) }
    }

    @Test
    fun `get region all stats with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionAllStats(any(), any()) } returns flow

        val flowRepository = repository.getRegionAllStats(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListRegionAndStatsEmptyData)
            }
        }
        verify { local.getRegionAllStats(any(), any()) }
    }

    @Test
    fun `get countries stats ordered by confirmed should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listCountryAndStats))
        }

        every { local.getCountriesStatsOrderByConfirmed() } returns flow

        val flowRepository = repository.getCountriesStatsOrderByConfirmed()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsSuccess))
            }
        }
        verify { local.getCountriesStatsOrderByConfirmed() }
    }

    @Test
    fun `get countries stats ordered by confirmed with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getCountriesStatsOrderByConfirmed() } returns flow

        val flowRepository = repository.getCountriesStatsOrderByConfirmed()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListCountryAndStatsEmptyData)
            }
        }
        verify { local.getCountriesStatsOrderByConfirmed() }
    }

    @Test
    fun `get regions stats ordered by confirmed should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listRegionStats))
        }

        every { local.getRegionsStatsOrderByConfirmed(any(), any()) } returns flow

        val flowRepository = repository.getRegionsStatsOrderByConfirmed(ID_REGION, DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionStatsSuccess))
            }
        }
        verify { local.getRegionsStatsOrderByConfirmed(any(), any()) }
    }

    @Test
    fun `get regions stats ordered by confirmed with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionsStatsOrderByConfirmed(any(), any()) } returns flow

        val flowRepository = repository.getRegionsStatsOrderByConfirmed(ID_REGION, DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListRegionStatsEmptyData)
            }
        }
        verify { local.getRegionsStatsOrderByConfirmed(any(), any()) }
    }

    @Test
    fun `get subregions stats ordered by confirmed should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listSubRegionStats))
        }

        every { local.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) } returns flow

        val flowRepository =
            repository.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionStatsSuccess))
            }
        }
        verify { local.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) }
    }

    @Test
    fun `get subregions stats ordered by confirmed with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) } returns flow

        val flowRepository =
            repository.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListSubRegionStatsEmptyData)
            }
        }
        verify { local.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) }
    }

    @Test
    fun `get regions all stats ordered by confirmed should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listRegionAndStats))
        }

        every { local.getRegionsAllStatsOrderByConfirmed(any()) } returns flow

        val flowRepository = repository.getRegionsAllStatsOrderByConfirmed(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionAndStatsSuccess))
            }
        }
        verify { local.getRegionsAllStatsOrderByConfirmed(any()) }
    }

    @Test
    fun `get regions all stats ordered by confirmed with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionsAllStatsOrderByConfirmed(any()) } returns flow

        val flowRepository = repository.getRegionsAllStatsOrderByConfirmed(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListRegionAndStatsEmptyData)
            }
        }
        verify { local.getRegionsAllStatsOrderByConfirmed(any()) }
    }

    @Test
    fun `get subregions all stats ordered by confirmed should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listSubRegionAndStats))
        }

        every { local.getSubRegionsAllStatsOrderByConfirmed(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAllStatsOrderByConfirmed(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionAndStatsSuccess))
            }
        }
        verify { local.getSubRegionsAllStatsOrderByConfirmed(any(), any()) }
    }

    @Test
    fun `get subregions all stats ordered by confirmed with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getSubRegionsAllStatsOrderByConfirmed(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAllStatsOrderByConfirmed(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListSubRegionAndStatsEmptyData)
            }
        }
        verify { local.getSubRegionsAllStatsOrderByConfirmed(any(), any()) }
    }

    @Test
    fun `get countries and stats with most confirmed should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listCountryAndStats))
        }

        every { local.getCountriesAndStatsWithMostConfirmed() } returns flow

        val flowRepository = repository.getCountriesAndStatsWithMostConfirmed()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsSuccess))
            }
        }
        verify { local.getCountriesAndStatsWithMostConfirmed() }
    }

    @Test
    fun `get countries and stats with most confirmed with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getCountriesAndStatsWithMostConfirmed() } returns flow

        val flowRepository = repository.getCountriesAndStatsWithMostConfirmed()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListCountryAndStatsEmptyData)
            }
        }
        verify { local.getCountriesAndStatsWithMostConfirmed() }
    }

    @Test
    fun `get regions and stats with most confirmed should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStats))
        }

        every { local.getRegionsAndStatsWithMostConfirmed(any()) } returns flow

        val flowRepository = repository.getRegionsAndStatsWithMostConfirmed(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
            }
        }
        verify { local.getRegionsAndStatsWithMostConfirmed(any()) }
    }

    @Test
    fun `get regions and stats with most confirmed with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionsAndStatsWithMostConfirmed(any()) } returns flow

        val flowRepository = repository.getRegionsAndStatsWithMostConfirmed(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateLineChartMostConfirmedListRegionAndStatsEmptySuccess)
            }
        }
        verify { local.getRegionsAndStatsWithMostConfirmed(any()) }
    }

    @Test
    fun `get subregions and stats with most confirmed should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStats))
        }

        every { local.getSubRegionsAndStatsWithMostConfirmed(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAndStatsWithMostConfirmed(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
            }
        }
        verify { local.getSubRegionsAndStatsWithMostConfirmed(any(), any()) }
    }

    @Test
    fun `get subregions and stats with most confirmed with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getSubRegionsAndStatsWithMostConfirmed(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAndStatsWithMostConfirmed(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess)
            }
        }
        verify { local.getSubRegionsAndStatsWithMostConfirmed(any(), any()) }
    }

    @Test
    fun `get countries and stats with most deaths should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listCountryAndStats))
        }

        every { local.getCountriesAndStatsWithMostDeaths() } returns flow

        val flowRepository = repository.getCountriesAndStatsWithMostDeaths()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsSuccess))
            }
        }
        verify { local.getCountriesAndStatsWithMostDeaths() }
    }

    @Test
    fun `get countries and stats with most deaths with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getCountriesAndStatsWithMostDeaths() } returns flow

        val flowRepository = repository.getCountriesAndStatsWithMostDeaths()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListCountryAndStatsEmptyData)
            }
        }
        verify { local.getCountriesAndStatsWithMostDeaths() }
    }

    @Test
    fun `get regions and stats with most deaths should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStats))
        }

        every { local.getRegionsAndStatsWithMostDeaths(any()) } returns flow

        val flowRepository = repository.getRegionsAndStatsWithMostDeaths(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
            }
        }
        verify { local.getRegionsAndStatsWithMostDeaths(any()) }
    }

    @Test
    fun `get regions and stats with most deaths with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionsAndStatsWithMostDeaths(any()) } returns flow

        val flowRepository = repository.getRegionsAndStatsWithMostDeaths(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateLineChartMostConfirmedListRegionAndStatsEmptySuccess)
            }
        }
        verify { local.getRegionsAndStatsWithMostDeaths(any()) }
    }

    @Test
    fun `get subregions and stats with most deaths should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStats))
        }

        every { local.getSubRegionsAndStatsWithMostDeaths(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAndStatsWithMostDeaths(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
            }
        }
        verify { local.getSubRegionsAndStatsWithMostDeaths(any(), any()) }
    }

    @Test
    fun `get subregions and stats with most deaths with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getSubRegionsAndStatsWithMostDeaths(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAndStatsWithMostDeaths(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess)
            }
        }
        verify { local.getSubRegionsAndStatsWithMostDeaths(any(), any()) }
    }

    @Test
    fun `get countries and stats with most open cases should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listCountryAndStats))
        }

        every { local.getCountriesAndStatsWithMostOpenCases() } returns flow

        val flowRepository = repository.getCountriesAndStatsWithMostOpenCases()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsSuccess))
            }
        }
        verify { local.getCountriesAndStatsWithMostOpenCases() }
    }

    @Test
    fun `get countries and stats with most open cases with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getCountriesAndStatsWithMostOpenCases() } returns flow

        val flowRepository = repository.getCountriesAndStatsWithMostOpenCases()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListCountryAndStatsEmptyData)
            }
        }
        verify { local.getCountriesAndStatsWithMostOpenCases() }
    }

    @Test
    fun `get regions and stats with most open cases should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStats))
        }

        every { local.getRegionsAndStatsWithMostOpenCases(any()) } returns flow

        val flowRepository = repository.getRegionsAndStatsWithMostOpenCases(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
            }
        }
        verify { local.getRegionsAndStatsWithMostOpenCases(any()) }
    }

    @Test
    fun `get regions and stats with most open cases with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionsAndStatsWithMostOpenCases(any()) } returns flow

        val flowRepository = repository.getRegionsAndStatsWithMostOpenCases(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateLineChartMostConfirmedListRegionAndStatsEmptySuccess)
            }
        }
        verify { local.getRegionsAndStatsWithMostOpenCases(any()) }
    }

    @Test
    fun `get subregions and stats with most open cases should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStats))
        }

        every { local.getSubRegionsAndStatsWithMostOpenCases(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAndStatsWithMostOpenCases(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
            }
        }
        verify { local.getSubRegionsAndStatsWithMostOpenCases(any(), any()) }
    }

    @Test
    fun `get subregions and stats with most open cases with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getSubRegionsAndStatsWithMostOpenCases(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAndStatsWithMostOpenCases(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess)
            }
        }
        verify { local.getSubRegionsAndStatsWithMostOpenCases(any(), any()) }
    }

    @Test
    fun `get countries and stats with most recovered should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listCountryAndStats))
        }

        every { local.getCountriesAndStatsWithMostRecovered() } returns flow

        val flowRepository = repository.getCountriesAndStatsWithMostRecovered()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsSuccess))
            }
        }
        verify { local.getCountriesAndStatsWithMostRecovered() }
    }

    @Test
    fun `get countries and stats with most recovered with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getCountriesAndStatsWithMostRecovered() } returns flow

        val flowRepository = repository.getCountriesAndStatsWithMostRecovered()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListCountryAndStatsEmptyData)
            }
        }
        verify { local.getCountriesAndStatsWithMostRecovered() }
    }

    @Test
    fun `get regions and stats with most recovered should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStats))
        }

        every { local.getRegionsAndStatsWithMostRecovered(any()) } returns flow

        val flowRepository = repository.getRegionsAndStatsWithMostRecovered(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
            }
        }
        verify { local.getRegionsAndStatsWithMostRecovered(any()) }
    }

    @Test
    fun `get regions and stats with most recovered with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionsAndStatsWithMostRecovered(any()) } returns flow

        val flowRepository = repository.getRegionsAndStatsWithMostRecovered(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateLineChartMostConfirmedListRegionAndStatsEmptySuccess)
            }
        }
        verify { local.getRegionsAndStatsWithMostRecovered(any()) }
    }

    @Test
    fun `get subregions and stats with most recovered should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStats))
        }

        every { local.getSubRegionsAndStatsWithMostRecovered(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAndStatsWithMostRecovered(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
            }
        }
        verify { local.getSubRegionsAndStatsWithMostRecovered(any(), any()) }
    }

    @Test
    fun `get subregions and stats with most recovered with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getSubRegionsAndStatsWithMostRecovered(any(), any()) } returns flow

        val flowRepository = repository.getSubRegionsAndStatsWithMostRecovered(ID_COUNTRY, ID_REGION)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess)
            }
        }
        verify { local.getSubRegionsAndStatsWithMostRecovered(any(), any()) }
    }

    @Test
    fun `get countries should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listCountry))
        }

        every { local.getCountries() } returns flow

        val flowRepository = repository.getCountries()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountrySuccess))
            }
        }
        verify { local.getCountries() }
    }

    @Test
    fun `get countries with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getCountries() } returns flow

        val flowRepository = repository.getCountries()

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListCountryEmptyData)
            }
        }
        verify { local.getCountries() }
    }

    @Test
    fun `get regions by country should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(listRegion))
        }

        every { local.getRegionsByCountry(any()) } returns flow

        val flowRepository = repository.getRegionsByCountry(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionSuccess))
            }
        }
        verify { local.getRegionsByCountry(any()) }
    }

    @Test
    fun `get regions by country with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionsByCountry(any()) } returns flow

        val flowRepository = repository.getRegionsByCountry(ID_COUNTRY)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateListRegionEmptyData)
            }
        }
        verify { local.getRegionsByCountry(any()) }
    }

    @Test
    fun `get country and stats by date should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(countryOneStats))
        }

        every { local.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowRepository = repository.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateCountryOneStatsSuccess))
            }
        }
        verify { local.getCountryAndStatsByDate(any(), any()) }
    }

    @Test
    fun `get country and stats by date with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowRepository = repository.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateCountryOneStatsEmptyData)
            }
        }
        verify { local.getCountryAndStatsByDate(any(), any()) }
    }

    @Test
    fun `get region and stats by date should return loading and success`() = runBlocking {
        val flow = flow {
            emit(Either.right(regionOneStats))
        }

        every { local.getRegionAndStatsByDate(any(), any(), any()) } returns flow

        val flowRepository = repository.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThat(data).isEqualTo(Either.right(stateRegionOneStatsSuccess))
            }
        }
        verify { local.getRegionAndStatsByDate(any(), any(), any()) }
    }

    @Test
    fun `get region and stats by date with empty database should return loading and empty success`() = runBlocking {
        val flow = flow {
            emit(Either.left(DomainError.DatabaseEmptyData))
        }

        every { local.getRegionAndStatsByDate(any(), any(), any()) } returns flow

        val flowRepository = repository.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, DATE)

        flowRepository.collectIndexed { index, data ->
            when (index) {
                0 -> assertThatIsEqualToState(data, stateCovidTrackerLoading)
                1 -> assertThatIsEqualToState(data, stateRegionOneStatsEmptyData)
            }
        }
        verify { local.getRegionAndStatsByDate(any(), any(), any()) }
    }

    @Test
    fun `get all dates should return a list with all dates`() = runBlockingTest {
        coEvery { local.getAllDates() } returns Either.right(DATES)

        val eitherUseCase = repository.getAllDates()

        coVerify { local.getAllDates() }
        eitherUseCase.map { dates ->
            assertThat(dates).hasSize(3)
            assertThat(dates).isEqualTo(DATES)
        }
    }

    @Test
    fun `get all dates with empty data should return an empty list`() = runBlockingTest {
        coEvery { local.getAllDates() } returns Either.right(listOf())

        val eitherUseCase = repository.getAllDates()

        coVerify { local.getAllDates() }
        eitherUseCase.map { dates ->
            assertThat(dates).isEmpty()
        }
    }

    @Test
    fun `add covid trackers should return unit`() = runBlockingTest {
        coEvery { local.populateDatabase(any()) } returns Unit

        repository.addCovidTrackers(listOf(covidTracker))

        coVerify { local.populateDatabase(any()) }
    }

    private fun <R, S> assertThatIsEqualToState(
        data: Either<StateError<DomainError>, R>,
        state: State<S>
    ) {
        assertThat(data.isRight()).isTrue()
        data.map { assertThat(it).isInstanceOf(state::class.java) }
    }
}