package com.jaimegc.covid19tracker.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetRegion
import com.jaimegc.covid19tracker.domain.usecase.GetRegionStats
import com.jaimegc.covid19tracker.domain.usecase.GetSubRegionStats
import com.jaimegc.covid19tracker.ui.base.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.country.CountryViewModel
import com.jaimegc.covid19tracker.utils.MainCoroutineRule
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.placeStateScreenErrorDatabaseEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCountryOneStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCountryOneStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCountryOneStatsSuccessData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateErrorDatabaseEmpty
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountrySuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionEmptySuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionStatsEmptySuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionStatsSuccessData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListSubRegionStatsEmptySuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListSubRegionStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListSubRegionStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListSubRegionStatsSuccessData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateRegionOneStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateRegionOneStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateRegionOneStatsSuccessData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateScreenListCountrySuccessData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateScreenListRegionEmptySuccessData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateScreenListRegionStatsEmptySuccessData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateScreenListRegionSuccessData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateScreenListSubRegionStatsEmptySuccessData
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
    fun `get countries should return loading and error if database is empty`() {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())

        val loading = captor.firstValue
        val error = captor.secondValue

        assertEquals(ScreenState.Loading, loading)
        assertTrue(error is ScreenState.Error)
        assertTrue((error as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorDatabaseEmptyData,
            (error.errorState as PlaceStateScreen.SomeError).data)
    }

    @Test
    fun `get regions by country should return loading and success`() {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionSuccess))
        }

        whenever(getRegion.getRegionsByCountry(any())).thenReturn(flow)

        countryViewModel.getRegionsByCountry("id_country")

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

        countryViewModel.getRegionsByCountry("id_country")

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

        countryViewModel.getListStats("id_country")

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateCountryOneStatsSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceStats)
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

        countryViewModel.getListStats("id_country")

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateCountryOneStatsSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceStats)
        assertEquals(stateScreenListRegionStatsEmptySuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStats).data)
    }

    @Test
    fun `get list stats for a country should return loading and error if database is empty`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsEmptySuccess))
        }

        whenever(getCountryStats.getCountryAndStatsByDate(any(), any())).thenReturn(countryStatsFlow)
        whenever(getRegionStats.getRegionsStatsOrderByConfirmed(any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getListStats("id_country")

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val loading = captor.firstValue
        val error = captor.thirdValue

        assertEquals(ScreenState.Loading, loading)
        assertTrue(error is ScreenState.Error)
        assertTrue((error as ScreenState.Error).errorState is PlaceStateScreen.SomeError)
        assertEquals(placeStateScreenErrorDatabaseEmptyData,
            (error.errorState as PlaceStateScreen.SomeError).data)
    }

    @Test
    fun `get list stats for a region with subregions should return loading and success`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsSuccess))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsSuccess))
        }

        whenever(getRegionStats.getRegionAndStatsByDate(any(), any(), any())).thenReturn(countryStatsFlow)
        whenever(getSubRegionStats.getSubRegionsStatsOrderByConfirmed(any(), any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getListStats("id_country", "id_region")

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateRegionOneStatsSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceStats)
        assertEquals(stateListSubRegionStatsSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStats).data)
    }

    @Test
    fun `get list stats for a region with empty subregions should return loading and success`() {
        val countryStatsFlow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsSuccess))
        }

        val regionStatsFlow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsEmptySuccess))
        }

        whenever(getRegionStats.getRegionAndStatsByDate(any(), any(), any())).thenReturn(countryStatsFlow)
        whenever(getSubRegionStats.getSubRegionsStatsOrderByConfirmed(any(), any(), any())).thenReturn(regionStatsFlow)

        countryViewModel.getListStats("id_country", "id_region")

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateRegionOneStatsSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceStats)
        assertEquals(stateScreenListSubRegionStatsEmptySuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStats).data)
    }

    @Test
    fun `get bar chart stats for a country with no region selected should return loading and success`() {

    }

    @Test
    fun `get bar chart stats for a country with empty regions should return loading and success`() {

    }

    @Test
    fun `get bar chart stats for a country should return loading and error if database is empty`() {

    }

    @Test
    fun `get bar chart stats for a region with subregions should return loading and success`() {

    }

    @Test
    fun `get bar chart stats for a region with empty subregions should return loading and success`() {

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

        countryViewModel.getListStats("id_country")

        verify(stateObserver, Mockito.times(4)).onChanged(captor.capture())

        val countryStatsLoading = captor.firstValue
        val countryStatsSuccess = captor.thirdValue
        val regionStatsLoading = captor.secondValue
        val regionStatsSuccess = captor.lastValue

        assertEquals(ScreenState.Loading, countryStatsLoading)
        assertEquals(ScreenState.Loading, regionStatsLoading)
        assertTrue(countryStatsSuccess is ScreenState.Render)
        assertTrue((countryStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceAndStats)
        assertEquals(stateCountryOneStatsSuccessData,
            (countryStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceAndStats).data)
        assertTrue(regionStatsSuccess is ScreenState.Render)
        assertTrue((regionStatsSuccess as ScreenState.Render).renderState is PlaceStateScreen.SuccessPlaceStats)
        assertEquals(stateListRegionStatsSuccessData,
            (regionStatsSuccess.renderState as PlaceStateScreen.SuccessPlaceStats).data)
    }

    @Test
    fun `get pie chart stats for a country with empty regions should return loading and success`() {

    }

    @Test
    fun `get pie chart stats for a country should return loading and error if database is empty`() {

    }

    @Test
    fun `get pie chart stats for a region with subregions should return loading and success`() {

    }

    @Test
    fun `get pie chart stats for a region with empty subregions should return loading and success`() {

    }

    @Test
    fun `get line chart stats for a country with no region selected should return loading and success`() {

    }

    @Test
    fun `get line chart stats for a country with empty regions should return loading and success`() {

    }

    @Test
    fun `get line chart stats for a country should return loading and error if database is empty`() {

    }

    @Test
    fun `get line chart stats for a region with subregions should return loading and success`() {

    }

    @Test
    fun `get line chart stats for a region with empty subregions should return loading and empty data`() {

    }
}