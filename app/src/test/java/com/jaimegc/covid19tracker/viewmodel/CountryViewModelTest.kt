package com.jaimegc.covid19tracker.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetRegion
import com.jaimegc.covid19tracker.domain.usecase.GetRegionStats
import com.jaimegc.covid19tracker.domain.usecase.GetSubRegionStats
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.base.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.country.CountryViewModel
import com.jaimegc.covid19tracker.utils.MainCoroutineRule
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.placeStateScreenErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsPieChartSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListRegionAndStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListSubRegionAndStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListRegionAndStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListSubRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListSubRegionAndStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListRegionAndStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListSubRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListSubRegionAndStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListRegionAndStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListSubRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListSubRegionAndStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryOnlyStatsBarChartSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryOnlyStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryOnlyStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountrySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsBarChartSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionOnlyStatsBarChartSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionOnlyStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionOnlyStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsPieChartSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsBarChartEmptySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsBarChartSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsPieChartSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListSubRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsPieChartSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListCountrySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListRegionEmptySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListRegionStatsEmptySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListRegionStatsPieChartEmptySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListRegionSuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListSubRegionStatsBarChartEmptySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListSubRegionStatsPieChartEmptySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListSubRegionStatsEmptySuccessData
import com.jaimegc.covid19tracker.utils.getOrAwaitValue
import com.jaimegc.covid19tracker.utils.observeForTesting
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class CountryViewModelTest {

    companion object {
        private const val ID_COUNTRY = "id_country"
        private const val ID_REGION = "id_region"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineRule()

    private val captor = argumentCaptor<ScreenState<PlaceStateScreen>>()

    private lateinit var countryViewModel: CountryViewModel
    private val getCountry: GetCountry = mock()
    private val getCountryStats: GetCountryStats = mock()
    private val getRegion: GetRegion = mock()
    private val getRegionStats: GetRegionStats = mock()
    private val getSubRegionStats: GetSubRegionStats = mock()

    private val stateObserver: Observer<ScreenState<PlaceStateScreen>> = mock()

    @Before
    fun setup() {
        countryViewModel =
            CountryViewModel(getCountry, getCountryStats, getRegion, getRegionStats, getSubRegionStats)
        countryViewModel.screenState.observeForever(stateObserver)
    }

    @Test
    fun `get countries should return loading and success`() {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountrySuccess))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val loading = captor.firstValue
        val success = captor.secondValue

        assertEquals(ScreenState.Loading, loading)
        assertTrue(success is ScreenState.Render)
        assertTrue((success as ScreenState.Render).renderState is PlaceStateScreen.SuccessSpinnerCountries)
        assertEquals(stateScreenListCountrySuccessData,
            (success.renderState as PlaceStateScreen.SuccessSpinnerCountries).data)
    }

    /*****************************************************************
     *  getOrAwaitValue and observeForTesting from Google repository *
     *****************************************************************/

    @Test
    fun `get countries should return loading and success using getOrAwaitValue & observeForTesting`() {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            delay(10)
            emit(Either.right(stateListCountrySuccess))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        countryViewModel.screenState.getOrAwaitValue {
            val loading = countryViewModel.screenState.value
            coroutineScope.advanceUntilIdle()
            val success = countryViewModel.screenState.value

            assertEquals(ScreenState.Loading, loading)
            assertTrue(success is ScreenState.Render)
            assertTrue((success as ScreenState.Render)
                .renderState is PlaceStateScreen.SuccessSpinnerCountries)
            assertEquals(stateScreenListCountrySuccessData,
                (success.renderState as PlaceStateScreen.SuccessSpinnerCountries).data)
        }

        countryViewModel.getCountries()

        countryViewModel.screenState.observeForTesting {
            val loading = countryViewModel.screenState.value
            coroutineScope.advanceUntilIdle()
            val success = countryViewModel.screenState.value

            assertEquals(ScreenState.Loading, loading)
            assertTrue(success is ScreenState.Render)
            assertTrue((success as ScreenState.Render)
                .renderState is PlaceStateScreen.SuccessSpinnerCountries)
            assertEquals(stateScreenListCountrySuccessData,
                (success.renderState as PlaceStateScreen.SuccessSpinnerCountries).data)
        }
    }

    /***********************************************************************************************/

    @Test
    fun `get countries with empty data should return loading and empty success`() {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountryEmptyData))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val loading = captor.firstValue
        val empty = captor.secondValue

        assertEquals(ScreenState.Loading, loading)
        assertEquals(ScreenState.EmptyData, empty)
    }

    @Test
    fun `get countries with database problem should return loading and unknown database error`() {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val loading = captor.firstValue
        val error = captor.secondValue

        assertEquals(ScreenState.Loading, loading)
        assertTrue(error is ScreenState.Error)
        assertTrue((error as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorUnknownDatabase,
            (error.errorState as PlaceStateScreen.SomeError).data)
    }

    @Test
    fun `get regions by country should return loading and success`() {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionSuccess))
        }

        whenever(getRegion.getRegionsByCountry(any())).thenReturn(flow)

        countryViewModel.getRegionsByCountry(ID_COUNTRY)

        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val loading = captor.firstValue
        val success = captor.secondValue

        assertEquals(ScreenState.Loading, loading)
        assertTrue(success is ScreenState.Render)
        assertTrue((success as ScreenState.Render).renderState is PlaceStateScreen.SuccessSpinnerRegions)
        assertEquals(stateScreenListRegionSuccessData,
            (success.renderState as PlaceStateScreen.SuccessSpinnerRegions).data)
    }

    @Test
    fun `get regions by country with empty data should return loading and success`() {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionEmptySuccess))
        }

        whenever(getRegion.getRegionsByCountry(any())).thenReturn(flow)

        countryViewModel.getRegionsByCountry(ID_COUNTRY)

        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val loading = captor.firstValue
        val success = captor.secondValue

        assertEquals(ScreenState.Loading, loading)
        assertTrue(success is ScreenState.Render)
        assertTrue((success as ScreenState.Render).renderState is PlaceStateScreen.SuccessSpinnerRegions)
        assertEquals(stateScreenListRegionEmptySuccessData,
            (success.renderState as PlaceStateScreen.SuccessSpinnerRegions).data)
    }

    @Test
    fun `get list stats for a country with no region selected should return loading and success`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsSuccess))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsSuccess))
        }

        whenever(getCountryStats.getCountryAndStatsByDate(any(), any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsStatsOrderByConfirmed(any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getListStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateCountryOneStatsSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStats)
        assertEquals(stateListRegionStatsSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStats).data)
    }

    @Test
    fun `get list stats for a country with empty regions should return loading and success`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsSuccess))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsEmptySuccess))
        }

        whenever(getCountryStats.getCountryAndStatsByDate(any(), any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsStatsOrderByConfirmed(any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getListStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateCountryOneStatsSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStats)
        assertEquals(stateScreenListRegionStatsEmptySuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStats).data)
    }

    @Test
    fun `get list stats for a country with database problem should return loading and unknown database error`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsEmptySuccess))
        }

        whenever(getCountryStats.getCountryAndStatsByDate(any(), any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsStatsOrderByConfirmed(any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getListStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val loading = captor.firstValue
        val error = captor.thirdValue

        assertEquals(ScreenState.Loading, loading)
        assertTrue(error is ScreenState.Error)
        assertTrue((error as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorUnknownDatabase,
            (error.errorState as PlaceStateScreen.SomeError).data)
    }

    @Test
    fun `get list stats for a region with subregions should return loading and success`() {
        val regionStatsFlow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsSuccess))
        }

        val subRegionStatsFlow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsSuccess))
        }

        whenever(getRegionStats.getRegionAndStatsByDate(any(), any(), any())).thenReturn(regionStatsFlow)
        whenever(getSubRegionStats.getSubRegionsStatsOrderByConfirmed(any(), any(), any())).thenReturn(subRegionStatsFlow)

        countryViewModel.getListStats(ID_COUNTRY, ID_REGION)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val regionStatsLoading = captor.firstValue
        val regionStatsSuccess = captor.thirdValue
        val subRegionStatsLoading = captor.secondValue
        val subRegionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertEquals(ScreenState.Loading, subRegionStatsLoading)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateRegionOneStatsSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(subRegionStatsSuccess is ScreenState.Render)
        assertTrue((subRegionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStats)
        assertEquals(stateListSubRegionStatsSuccessData,
            (subRegionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStats).data)
    }

    @Test
    fun `get list stats for a region with empty subregions should return loading and success`() {
        val regionStatsFlow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsSuccess))
        }

        val subRegionStatsFlow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsEmptySuccess))
        }

        whenever(getRegionStats.getRegionAndStatsByDate(any(), any(), any())).thenReturn(regionStatsFlow)
        whenever(getSubRegionStats.getSubRegionsStatsOrderByConfirmed(any(), any(), any())).thenReturn(subRegionStatsFlow)

        countryViewModel.getListStats(ID_COUNTRY, ID_REGION)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val regionStatsLoading = captor.firstValue
        val regionStatsSuccess = captor.thirdValue
        val subRegionStatsLoading = captor.secondValue
        val subRegionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertEquals(ScreenState.Loading, subRegionStatsLoading)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateRegionOneStatsSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(subRegionStatsSuccess is ScreenState.Render)
        assertTrue((subRegionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStats)
        assertEquals(stateScreenListSubRegionStatsEmptySuccessData,
            (subRegionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStats).data)
    }

    @Test
    fun `get bar chart stats for a country with no region selected should return loading and success`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateListCountryOnlyStatsLoading))
            emit(Either.right(stateListCountryOnlyStatsSuccess))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.right(stateListRegionAndStatsSuccess))
        }

        whenever(getCountryStats.getCountryAllStats(any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsAllStatsOrderByConfirmed(any())).thenReturn(regionStatsFlow)

        countryViewModel.getBarChartStats(ID_COUNTRY)

        // We use Flow combine(), so loadings are going to be lost
        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val countryStatsSuccess = captor.firstValue
        val regionStatsSuccess = captor.secondValue

        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceTotalStatsBarChart)
        assertEquals(stateListCountryOnlyStatsBarChartSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceTotalStatsBarChart).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsBarChart)
        assertEquals(stateListRegionAndStatsBarChartSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsBarChart).data)
    }

    @Test
    fun `get bar chart stats for a country with empty regions should return loading and success`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateListCountryOnlyStatsLoading))
            emit(Either.right(stateListCountryOnlyStatsSuccess))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.right(stateListRegionAndStatsEmptySuccess))
        }

        whenever(getCountryStats.getCountryAllStats(any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsAllStatsOrderByConfirmed(any())).thenReturn(regionStatsFlow)

        countryViewModel.getBarChartStats(ID_COUNTRY)

        // We use Flow combine(), so loadings are going to be lost
        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val countryStatsSuccess = captor.firstValue
        val regionStatsSuccess = captor.secondValue

        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceTotalStatsBarChart)
        assertEquals(stateListCountryOnlyStatsBarChartSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceTotalStatsBarChart).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsBarChart)
        assertEquals(stateScreenListSubRegionStatsBarChartEmptySuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsBarChart).data)
    }

    @Test
    fun `get bar chart stats for a country with database problem should return loading and unknown database error`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateListCountryOnlyStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.right(stateListRegionAndStatsEmptySuccess))
        }

        whenever(getCountryStats.getCountryAllStats(any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsAllStatsOrderByConfirmed(any())).thenReturn(regionStatsFlow)

        countryViewModel.getBarChartStats(ID_COUNTRY)

        // We use Flow combine(), so loadings are going to be lost
        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val error = captor.firstValue

        assertTrue(error is ScreenState.Error)
        assertTrue((error as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorUnknownDatabase,
            (error.errorState as PlaceStateScreen.SomeError).data)
    }

    @Test
    fun `get bar chart stats for a region with subregions should return loading and success`() {
        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionOnlyStatsLoading))
            emit(Either.right(stateListRegionOnlyStatsSuccess))
        }

        val subRegionStatsFlow = flow {
            emit(Either.right(stateListSubRegionAndStatsLoading))
            emit(Either.right(stateListSubRegionAndStatsSuccess))
        }

        whenever(getRegionStats.getRegionAllStats(any(), any())).thenReturn(regionStatsFlow)
        whenever(getSubRegionStats.getSubRegionsAllStatsOrderByConfirmed(any(), any())).thenReturn(subRegionStatsFlow)

        countryViewModel.getBarChartStats(ID_COUNTRY, ID_REGION)

        // We use Flow combine(), so loadings are going to be lost
        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val regionStatsSuccess = captor.firstValue
        val subRegionStatsSuccess = captor.secondValue

        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceTotalStatsBarChart)
        assertEquals(stateListRegionOnlyStatsBarChartSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceTotalStatsBarChart).data)
        assertTrue(subRegionStatsSuccess is ScreenState.Render)
        assertTrue((subRegionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsBarChart)
        assertEquals(stateListSubRegionAndStatsBarChartSuccessData,
            (subRegionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsBarChart).data)
    }

    @Test
    fun `get bar chart stats for a region with empty subregions should return loading and success`() {
        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionOnlyStatsLoading))
            emit(Either.right(stateListRegionOnlyStatsSuccess))
        }

        val subRegionStatsFlow = flow {
            emit(Either.right(stateListSubRegionAndStatsLoading))
            emit(Either.right(stateListSubRegionAndStatsEmptySuccess))
        }

        whenever(getRegionStats.getRegionAllStats(any(), any())).thenReturn(regionStatsFlow)
        whenever(getSubRegionStats.getSubRegionsAllStatsOrderByConfirmed(any(), any())).thenReturn(subRegionStatsFlow)

        countryViewModel.getBarChartStats(ID_COUNTRY, ID_REGION)

        // We use Flow combine(), so loadings are going to be lost
        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val regionStatsSuccess = captor.firstValue
        val subRegionStatsSuccess = captor.secondValue

        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceTotalStatsBarChart)
        assertEquals(stateListRegionOnlyStatsBarChartSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceTotalStatsBarChart).data)
        assertTrue(subRegionStatsSuccess is ScreenState.Render)
        assertTrue((subRegionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsBarChart)
        assertEquals(stateListSubRegionAndStatsBarChartEmptySuccessData,
            (subRegionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsBarChart).data)
    }

    @Test
    fun `get pie chart stats for a country with no region selected should return loading and success`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsSuccess))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsSuccess))
        }

        whenever(getCountryStats.getCountryAndStatsByDate(any(), any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsStatsOrderByConfirmed(any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getPieChartStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceTotalStatsPieChart)
        assertEquals(stateCountryOneStatsPieChartSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceTotalStatsPieChart).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceAndStatsPieChart)
        assertEquals(stateListRegionStatsPieChartSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStatsPieChart).data)
    }

    @Test
    fun `get pie chart stats for a country with empty regions should return loading and success`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsSuccess))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsEmptySuccess))
        }

        whenever(getCountryStats.getCountryAndStatsByDate(any(), any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsStatsOrderByConfirmed(any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getPieChartStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceTotalStatsPieChart)
        assertEquals(stateCountryOneStatsPieChartSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceTotalStatsPieChart).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceAndStatsPieChart)
        assertEquals(stateScreenListRegionStatsPieChartEmptySuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStatsPieChart).data)
    }

    @Test
    fun `get pie chart stats for a country with database problem should return loading and unknown database error`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsEmptySuccess))
        }

        whenever(getCountryStats.getCountryAndStatsByDate(any(), any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsStatsOrderByConfirmed(any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getPieChartStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val loading = captor.firstValue
        val error = captor.thirdValue

        assertEquals(ScreenState.Loading, loading)
        assertTrue(error is ScreenState.Error)
        assertTrue((error as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorUnknownDatabase,
            (error.errorState as PlaceStateScreen.SomeError).data)
    }

    @Test
    fun `get pie chart stats for a region with subregions should return loading and success`() {
        val regionStatsFlow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsSuccess))
        }

        val subRegionStatsFlow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsSuccess))
        }

        whenever(getRegionStats.getRegionAndStatsByDate(any(), any(), any())).thenReturn(regionStatsFlow)
        whenever(getSubRegionStats.getSubRegionsStatsOrderByConfirmed(any(), any(), any())).thenReturn(subRegionStatsFlow)

        countryViewModel.getPieChartStats(ID_COUNTRY, ID_REGION)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val regionStatsLoading = captor.firstValue
        val regionStatsSuccess = captor.thirdValue
        val subRegionStatsLoading = captor.secondValue
        val subRegionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertEquals(ScreenState.Loading, subRegionStatsLoading)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceTotalStatsPieChart)
        assertEquals(stateRegionOneStatsPieChartSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceTotalStatsPieChart).data)
        assertTrue(subRegionStatsSuccess is ScreenState.Render)
        assertTrue((subRegionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceAndStatsPieChart)
        assertEquals(stateListSubRegionStatsPieChartSuccessData,
            (subRegionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStatsPieChart).data)
    }

    @Test
    fun `get pie chart stats for a region with empty subregions should return loading and success`() {
        val regionStatsFlow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsSuccess))
        }

        val subRegionStatsFlow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsEmptySuccess))
        }

        whenever(getRegionStats.getRegionAndStatsByDate(any(), any(), any())).thenReturn(regionStatsFlow)
        whenever(getSubRegionStats.getSubRegionsStatsOrderByConfirmed(any(), any(), any())).thenReturn(subRegionStatsFlow)

        countryViewModel.getPieChartStats(ID_COUNTRY, ID_REGION)

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val regionStatsLoading = captor.firstValue
        val regionStatsSuccess = captor.thirdValue
        val subRegionStatsLoading = captor.secondValue
        val subRegionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertEquals(ScreenState.Loading, subRegionStatsLoading)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceTotalStatsPieChart)
        assertEquals(stateRegionOneStatsPieChartSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceTotalStatsPieChart).data)
        assertTrue(subRegionStatsSuccess is ScreenState.Render)
        assertTrue((subRegionStatsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceAndStatsPieChart)
        assertEquals(stateScreenListSubRegionStatsPieChartEmptySuccessData,
            (subRegionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStatsPieChart).data)
    }

    @Test
    fun `get line chart stats for a country with no region selected should return loading and success`() {
        val regionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
        }

        val regionStatsMostDeathsFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostDeathsListRegionAndStatsSuccess))
        }

        val regionStatsMostRecoveredFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostRecoveredListRegionAndStatsSuccess))
        }

        val regionStatsMostOpenCasesFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostOpenCasesListRegionAndStatsSuccess))
        }

        whenever(getRegionStats.getRegionsAndStatsWithMostConfirmed(any())).thenReturn(regionStatsMostConfirmedFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostDeaths(any())).thenReturn(regionStatsMostDeathsFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostRecovered(any())).thenReturn(regionStatsMostRecoveredFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostOpenCases(any())).thenReturn(regionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(8)).onChanged(captor.capture())

        val regionStatsMostConfirmedLoading = captor.allValues[0]
        val regionStatsMostConfirmedSuccess = captor.allValues[1]
        val regionStatsMostDeathsLoading = captor.allValues[2]
        val regionStatsMostDeathsSuccess = captor.allValues[3]
        val regionStatsMostRecoveredLoading = captor.allValues[4]
        val regionStatsMostRecoveredSuccess = captor.allValues[5]
        val regionStatsMostOpenCasesLoading = captor.allValues[6]
        val regionStatsMostOpenCasesSuccess = captor.allValues[7]

        assertEquals(ScreenState.Loading, regionStatsMostConfirmedLoading)
        assertEquals(ScreenState.Loading, regionStatsMostDeathsLoading)
        assertEquals(ScreenState.Loading, regionStatsMostRecoveredLoading)
        assertEquals(ScreenState.Loading, regionStatsMostOpenCasesLoading)
        assertTrue(regionStatsMostConfirmedSuccess is ScreenState.Render)
        assertTrue((regionStatsMostConfirmedSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts)
        assertEquals(stateLineChartMostConfirmedListRegionAndStatsSuccessData[MenuItemViewType.LineChartMostConfirmed],
            (regionStatsMostConfirmedSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
                .data[MenuItemViewType.LineChartMostConfirmed])
        assertTrue(regionStatsMostDeathsSuccess is ScreenState.Render)
        assertTrue((regionStatsMostDeathsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts)
        assertEquals(stateLineChartMostDeathsListRegionAndStatsSuccessData[MenuItemViewType.LineChartMostDeaths],
            (regionStatsMostDeathsSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
                .data[MenuItemViewType.LineChartMostDeaths])
        assertTrue(regionStatsMostRecoveredSuccess is ScreenState.Render)
        assertTrue((regionStatsMostRecoveredSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts)
        assertEquals(stateLineChartMostRecoveredListRegionAndStatsSuccessData[MenuItemViewType.LineChartMostRecovered],
            (regionStatsMostRecoveredSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
                .data[MenuItemViewType.LineChartMostRecovered])
        assertTrue(regionStatsMostOpenCasesSuccess is ScreenState.Render)
        assertTrue((regionStatsMostOpenCasesSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts)
        assertEquals(stateLineChartMostOpenCasesListRegionAndStatsSuccessData[MenuItemViewType.LineChartMostOpenCases],
            (regionStatsMostOpenCasesSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
                .data[MenuItemViewType.LineChartMostOpenCases])
    }

    @Test
    fun `get line chart stats for a country with empty regions should return loading and empty data success`() {
        val regionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStatsEmptySuccess))
        }

        val regionStatsMostDeathsFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostDeathsListRegionAndStatsEmptySuccess))
        }

        val regionStatsMostRecoveredFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostRecoveredListRegionAndStatsEmptySuccess))
        }

        val regionStatsMostOpenCasesFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostOpenCasesListRegionAndStatsEmptySuccess))
        }

        whenever(getRegionStats.getRegionsAndStatsWithMostConfirmed(any())).thenReturn(regionStatsMostConfirmedFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostDeaths(any())).thenReturn(regionStatsMostDeathsFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostRecovered(any())).thenReturn(regionStatsMostRecoveredFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostOpenCases(any())).thenReturn(regionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(8)).onChanged(captor.capture())

        val regionStatsMostConfirmedLoading = captor.allValues[0]
        val regionStatsMostConfirmedEmptyDataSuccess = captor.allValues[1]
        val regionStatsMostDeathsLoading = captor.allValues[2]
        val regionStatsMostDeathsEmptyDataSuccess = captor.allValues[3]
        val regionStatsMostRecoveredLoading = captor.allValues[4]
        val regionStatsMostRecoveredEmptyDataSuccess = captor.allValues[5]
        val regionStatsMostOpenCasesLoading = captor.allValues[6]
        val regionStatsMostOpenCasesEmptyDataSuccess = captor.allValues[7]

        assertEquals(ScreenState.Loading, regionStatsMostConfirmedLoading)
        assertEquals(ScreenState.Loading, regionStatsMostDeathsLoading)
        assertEquals(ScreenState.Loading, regionStatsMostRecoveredLoading)
        assertEquals(ScreenState.Loading, regionStatsMostOpenCasesLoading)
        assertTrue(regionStatsMostConfirmedEmptyDataSuccess is ScreenState.EmptyData)
        assertTrue(regionStatsMostDeathsEmptyDataSuccess is ScreenState.EmptyData)
        assertTrue(regionStatsMostRecoveredEmptyDataSuccess is ScreenState.EmptyData)
        assertTrue(regionStatsMostOpenCasesEmptyDataSuccess is ScreenState.EmptyData)
    }

    @Test
    fun `get line chart stats for a country with database problem should return loading and unknown database error`() {
        val regionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsMostDeathsFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsMostRecoveredFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsMostOpenCasesFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getRegionStats.getRegionsAndStatsWithMostConfirmed(any())).thenReturn(regionStatsMostConfirmedFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostDeaths(any())).thenReturn(regionStatsMostDeathsFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostRecovered(any())).thenReturn(regionStatsMostRecoveredFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostOpenCases(any())).thenReturn(regionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY)

        verify(stateObserver, Mockito.times(8)).onChanged(captor.capture())

        val regionStatsMostConfirmedLoading = captor.allValues[0]
        val regionStatsMostConfirmedError = captor.allValues[1]
        val regionStatsMostDeathsLoading = captor.allValues[2]
        val regionStatsMostDeathsError = captor.allValues[3]
        val regionStatsMostRecoveredLoading = captor.allValues[4]
        val regionStatsMostRecoveredError = captor.allValues[5]
        val regionStatsMostOpenCasesLoading = captor.allValues[6]
        val regionStatsMostOpenCasesError = captor.allValues[7]

        assertEquals(ScreenState.Loading, regionStatsMostConfirmedLoading)
        assertEquals(ScreenState.Loading, regionStatsMostDeathsLoading)
        assertEquals(ScreenState.Loading, regionStatsMostRecoveredLoading)
        assertEquals(ScreenState.Loading, regionStatsMostOpenCasesLoading)
        assertTrue(regionStatsMostConfirmedError is ScreenState.Error)
        assertTrue((regionStatsMostConfirmedError as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorUnknownDatabase,
            (regionStatsMostConfirmedError.errorState as PlaceStateScreen.SomeError).data)

        assertTrue(regionStatsMostDeathsError is ScreenState.Error)
        assertTrue((regionStatsMostDeathsError as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorUnknownDatabase,
            (regionStatsMostDeathsError.errorState as PlaceStateScreen.SomeError).data)

        assertTrue(regionStatsMostRecoveredError is ScreenState.Error)
        assertTrue((regionStatsMostRecoveredError as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorUnknownDatabase,
            (regionStatsMostRecoveredError.errorState as PlaceStateScreen.SomeError).data)

        assertTrue(regionStatsMostOpenCasesError is ScreenState.Error)
        assertTrue((regionStatsMostOpenCasesError as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorUnknownDatabase,
            (regionStatsMostOpenCasesError.errorState as PlaceStateScreen.SomeError).data)
    }

    @Test
    fun `get line chart stats for a region with subregions should return loading and success`() {
        val subRegionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
        }

        val subRegionStatsMostDeathsFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostDeathsListSubRegionAndStatsSuccess))
        }

        val subRegionStatsMostRecoveredFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostRecoveredListSubRegionAndStatsSuccess))
        }

        val subRegionStatsMostOpenCasesFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostOpenCasesListSubRegionAndStatsSuccess))
        }

        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(any(), any())).thenReturn(subRegionStatsMostConfirmedFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(any(), any())).thenReturn(subRegionStatsMostDeathsFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(any(), any())).thenReturn(subRegionStatsMostRecoveredFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(any(), any())).thenReturn(subRegionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY, ID_REGION)

        verify(stateObserver, Mockito.times(8)).onChanged(captor.capture())

        val subRegionStatsMostConfirmedLoading = captor.allValues[0]
        val subRegionStatsMostConfirmedSuccess = captor.allValues[1]
        val subRegionStatsMostDeathsLoading = captor.allValues[2]
        val subRegionStatsMostDeathsSuccess = captor.allValues[3]
        val subRegionStatsMostRecoveredLoading = captor.allValues[4]
        val subRegionStatsMostRecoveredSuccess = captor.allValues[5]
        val subRegionStatsMostOpenCasesLoading = captor.allValues[6]
        val subRegionStatsMostOpenCasesSuccess = captor.allValues[7]

        assertEquals(ScreenState.Loading, subRegionStatsMostConfirmedLoading)
        assertEquals(ScreenState.Loading, subRegionStatsMostDeathsLoading)
        assertEquals(ScreenState.Loading, subRegionStatsMostRecoveredLoading)
        assertEquals(ScreenState.Loading, subRegionStatsMostOpenCasesLoading)
        assertTrue(subRegionStatsMostConfirmedSuccess is ScreenState.Render)
        assertTrue((subRegionStatsMostConfirmedSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts)
        assertEquals(stateLineChartMostConfirmedListSubRegionAndStatsSuccessData[MenuItemViewType.LineChartMostConfirmed],
            (subRegionStatsMostConfirmedSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
                .data[MenuItemViewType.LineChartMostConfirmed])
        assertTrue(subRegionStatsMostDeathsSuccess is ScreenState.Render)
        assertTrue((subRegionStatsMostDeathsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts)
        assertEquals(stateLineChartMostDeathsListSubRegionAndStatsSuccessData[MenuItemViewType.LineChartMostDeaths],
            (subRegionStatsMostDeathsSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
                .data[MenuItemViewType.LineChartMostDeaths])
        assertTrue(subRegionStatsMostRecoveredSuccess is ScreenState.Render)
        assertTrue((subRegionStatsMostRecoveredSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts)
        assertEquals(stateLineChartMostRecoveredListSubRegionAndStatsSuccessData[MenuItemViewType.LineChartMostRecovered],
            (subRegionStatsMostRecoveredSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
                .data[MenuItemViewType.LineChartMostRecovered])
        assertTrue(subRegionStatsMostOpenCasesSuccess is ScreenState.Render)
        assertTrue((subRegionStatsMostOpenCasesSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts)
        assertEquals(stateLineChartMostOpenCasesListSubRegionAndStatsSuccessData[MenuItemViewType.LineChartMostOpenCases],
            (subRegionStatsMostOpenCasesSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
                .data[MenuItemViewType.LineChartMostOpenCases])
    }

    @Test
    fun `get line chart stats for a region with empty subregions should return loading and empty data success`() {
        val subRegionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess))
        }

        val subRegionStatsMostDeathsFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostDeathsListSubRegionAndStatsEmptySuccess))
        }

        val subRegionStatsMostRecoveredFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostRecoveredListSubRegionAndStatsEmptySuccess))
        }

        val subRegionStatsMostOpenCasesFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostOpenCasesListSubRegionAndStatsEmptySuccess))
        }

        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(any(), any())).thenReturn(subRegionStatsMostConfirmedFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(any(), any())).thenReturn(subRegionStatsMostDeathsFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(any(), any())).thenReturn(subRegionStatsMostRecoveredFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(any(), any())).thenReturn(subRegionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY, ID_REGION)

        verify(stateObserver, Mockito.times(8)).onChanged(captor.capture())

        val subRegionStatsMostConfirmedLoading = captor.allValues[0]
        val subRegionStatsMostConfirmedEmptyDataSuccess = captor.allValues[1]
        val subRegionStatsMostDeathsLoading = captor.allValues[2]
        val subRegionStatsMostDeathsEmptyDataSuccess = captor.allValues[3]
        val subRegionStatsMostRecoveredLoading = captor.allValues[4]
        val subRegionStatsMostRecoveredEmptyDataSuccess = captor.allValues[5]
        val subRegionStatsMostOpenCasesLoading = captor.allValues[6]
        val subRegionStatsMostOpenCasesEmptyDataSuccess = captor.allValues[7]

        assertEquals(ScreenState.Loading, subRegionStatsMostConfirmedLoading)
        assertEquals(ScreenState.Loading, subRegionStatsMostDeathsLoading)
        assertEquals(ScreenState.Loading, subRegionStatsMostRecoveredLoading)
        assertEquals(ScreenState.Loading, subRegionStatsMostOpenCasesLoading)
        assertTrue(subRegionStatsMostConfirmedEmptyDataSuccess is ScreenState.EmptyData)
        assertTrue(subRegionStatsMostDeathsEmptyDataSuccess is ScreenState.EmptyData)
        assertTrue(subRegionStatsMostRecoveredEmptyDataSuccess is ScreenState.EmptyData)
        assertTrue(subRegionStatsMostOpenCasesEmptyDataSuccess is ScreenState.EmptyData)
    }
}