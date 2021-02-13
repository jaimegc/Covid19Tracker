package com.jaimegc.covid19tracker.viewmodel.kotest

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
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.placeStateScreenErrorUnknownDatabase
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
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountrySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListSubRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListCountrySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListRegionEmptySuccessData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenListRegionSuccessData
import com.jaimegc.covid19tracker.util.getOrAwaitValue
import com.jaimegc.covid19tracker.util.kotest.ProjectConfig
import com.jaimegc.covid19tracker.util.observeForTesting
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

private const val ID_COUNTRY = "id_country"
private const val ID_REGION = "id_region"

class CountryViewModelKotestTest : FunSpec({

    lateinit var countryViewModel: CountryViewModel
    val getCountry: GetCountry = mock()
    val getCountryStats: GetCountryStats = mock()
    val getRegion: GetRegion = mock()
    val getRegionStats: GetRegionStats = mock()
    val getSubRegionStats: GetSubRegionStats = mock()

    beforeTest {
        countryViewModel =
            CountryViewModel(getCountry, getCountryStats, getRegion, getRegionStats, getSubRegionStats)
    }

    test("get countries should return loading and success") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            delay(10)
            emit(Either.right(stateListCountrySuccess))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        val loading = countryViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val success = countryViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        (success is ScreenState.Render) shouldBe true
        ((success as ScreenState.Render).renderState is PlaceStateScreen.SuccessSpinnerCountries) shouldBe true
        ((success.renderState as PlaceStateScreen.SuccessSpinnerCountries).data) shouldBe
            stateScreenListCountrySuccessData
    }

    /*****************************************************************
     *  getOrAwaitValue and observeForTesting from Google repository *
     *****************************************************************/

    test("get countries should return loading and success using getOrAwaitValue & observeForTesting") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            delay(10)
            emit(Either.right(stateListCountrySuccess))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        countryViewModel.screenState.getOrAwaitValue {
            val loading = countryViewModel.screenState.value
            ProjectConfig.advanceUntilIdle()
            val success = countryViewModel.screenState.value

            loading shouldBe ScreenState.Loading
            (success is ScreenState.Render) shouldBe true
            ((success as ScreenState.Render).renderState is PlaceStateScreen.SuccessSpinnerCountries) shouldBe true
            ((success.renderState as PlaceStateScreen.SuccessSpinnerCountries).data) shouldBe
                stateScreenListCountrySuccessData
        }

        countryViewModel.getCountries()

        countryViewModel.screenState.observeForTesting {
            val loading = countryViewModel.screenState.value
            ProjectConfig.advanceUntilIdle()
            val success = countryViewModel.screenState.value

            loading shouldBe ScreenState.Loading
            (success is ScreenState.Render) shouldBe true
            ((success as ScreenState.Render).renderState is PlaceStateScreen.SuccessSpinnerCountries) shouldBe true
            ((success.renderState as PlaceStateScreen.SuccessSpinnerCountries).data) shouldBe
                stateScreenListCountrySuccessData
        }
    }

    /***********************************************************************************************/

    test("get countries with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            delay(10)
            emit(Either.right(stateListCountryEmptyData))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        val loading = countryViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val empty = countryViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        empty shouldBe ScreenState.EmptyData
    }

    test("get countries with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getCountry.getCountries()).thenReturn(flow)

        countryViewModel.getCountries()

        val loading = countryViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val error = countryViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        (error is ScreenState.Error) shouldBe true
        ((error as ScreenState.Error).errorState is PlaceStateScreen.SomeError) shouldBe true
        ((error.errorState as PlaceStateScreen.SomeError).data) shouldBe
            placeStateScreenErrorUnknownDatabase
    }

    test("get regions by country should return loading and success") {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            delay(10)
            emit(Either.right(stateListRegionSuccess))
        }

        whenever(getRegion.getRegionsByCountry(any())).thenReturn(flow)

        countryViewModel.getRegionsByCountry(ID_COUNTRY)

        val loading = countryViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val success = countryViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        (success is ScreenState.Render) shouldBe true
        ((success as ScreenState.Render).renderState is PlaceStateScreen.SuccessSpinnerRegions) shouldBe true
        ((success.renderState as PlaceStateScreen.SuccessSpinnerRegions).data) shouldBe
            stateScreenListRegionSuccessData
    }

    test("get regions by country with empty data should return loading and success") {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            delay(10)
            emit(Either.right(stateListRegionEmptySuccess))
        }

        whenever(getRegion.getRegionsByCountry(any())).thenReturn(flow)

        countryViewModel.getRegionsByCountry(ID_COUNTRY)

        val loading = countryViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val success = countryViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        (success is ScreenState.Render) shouldBe true
        ((success as ScreenState.Render).renderState is PlaceStateScreen.SuccessSpinnerRegions) shouldBe true
        ((success.renderState as PlaceStateScreen.SuccessSpinnerRegions).data) shouldBe
            stateScreenListRegionEmptySuccessData
    }

    test("get line chart stats for a country with no region selected should return loading and success") {
        val regionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
        }

        val regionStatsMostDeathsFlow = flow {
            delay(20)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostDeathsListRegionAndStatsSuccess))
        }

        val regionStatsMostRecoveredFlow = flow {
            delay(40)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostRecoveredListRegionAndStatsSuccess))
        }

        val regionStatsMostOpenCasesFlow = flow {
            delay(60)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostOpenCasesListRegionAndStatsSuccess))
        }

        whenever(getRegionStats.getRegionsAndStatsWithMostConfirmed(any())).thenReturn(regionStatsMostConfirmedFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostDeaths(any())).thenReturn(regionStatsMostDeathsFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostRecovered(any())).thenReturn(regionStatsMostRecoveredFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostOpenCases(any())).thenReturn(regionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY)

        val regionStatsMostConfirmedLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostConfirmedSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostDeathsLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostDeathsSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostRecoveredLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostRecoveredSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostOpenCasesLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostOpenCasesSuccess = countryViewModel.screenState.value

        regionStatsMostConfirmedLoading shouldBe ScreenState.Loading
        regionStatsMostDeathsLoading shouldBe ScreenState.Loading
        regionStatsMostRecoveredLoading shouldBe ScreenState.Loading
        regionStatsMostOpenCasesLoading shouldBe ScreenState.Loading
        (regionStatsMostConfirmedSuccess is ScreenState.Render) shouldBe true
        ((regionStatsMostConfirmedSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts) shouldBe true
        ((regionStatsMostConfirmedSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
            .data[MenuItemViewType.LineChartMostConfirmed]) shouldBe
                stateLineChartMostConfirmedListRegionAndStatsSuccessData[MenuItemViewType.LineChartMostConfirmed]
        (regionStatsMostDeathsSuccess is ScreenState.Render) shouldBe true
        ((regionStatsMostDeathsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts) shouldBe true
        ((regionStatsMostDeathsSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
            .data[MenuItemViewType.LineChartMostDeaths]) shouldBe
                stateLineChartMostDeathsListRegionAndStatsSuccessData[MenuItemViewType.LineChartMostDeaths]
        (regionStatsMostRecoveredSuccess is ScreenState.Render) shouldBe true
        ((regionStatsMostRecoveredSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts) shouldBe true
        ((regionStatsMostRecoveredSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
            .data[MenuItemViewType.LineChartMostRecovered]) shouldBe
                stateLineChartMostRecoveredListRegionAndStatsSuccessData[MenuItemViewType.LineChartMostRecovered]

        (regionStatsMostOpenCasesSuccess is ScreenState.Render) shouldBe true
        ((regionStatsMostOpenCasesSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts) shouldBe true
        ((regionStatsMostOpenCasesSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
            .data[MenuItemViewType.LineChartMostOpenCases]) shouldBe
                stateLineChartMostOpenCasesListRegionAndStatsSuccessData[MenuItemViewType.LineChartMostOpenCases]
    }

    test("get line chart stats for a country with empty regions should return loading and empty data success") {
        val regionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStatsEmptySuccess))
        }

        val regionStatsMostDeathsFlow = flow {
            delay(20)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostDeathsListRegionAndStatsEmptySuccess))
        }

        val regionStatsMostRecoveredFlow = flow {
            delay(40)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostRecoveredListRegionAndStatsEmptySuccess))
        }

        val regionStatsMostOpenCasesFlow = flow {
            delay(60)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostOpenCasesListRegionAndStatsEmptySuccess))
        }

        whenever(getRegionStats.getRegionsAndStatsWithMostConfirmed(any())).thenReturn(regionStatsMostConfirmedFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostDeaths(any())).thenReturn(regionStatsMostDeathsFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostRecovered(any())).thenReturn(regionStatsMostRecoveredFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostOpenCases(any())).thenReturn(regionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY)

        val regionStatsMostConfirmedLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostConfirmedEmptyDataSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostDeathsLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostDeathsEmptyDataSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostRecoveredLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostRecoveredEmptyDataSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostOpenCasesLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostOpenCasesEmptyDataSuccess = countryViewModel.screenState.value

        regionStatsMostConfirmedLoading shouldBe ScreenState.Loading
        regionStatsMostDeathsLoading shouldBe ScreenState.Loading
        regionStatsMostRecoveredLoading shouldBe ScreenState.Loading
        regionStatsMostOpenCasesLoading shouldBe ScreenState.Loading
        (regionStatsMostConfirmedEmptyDataSuccess is ScreenState.EmptyData) shouldBe true
        (regionStatsMostDeathsEmptyDataSuccess is ScreenState.EmptyData) shouldBe true
        (regionStatsMostRecoveredEmptyDataSuccess is ScreenState.EmptyData) shouldBe true
        (regionStatsMostOpenCasesEmptyDataSuccess is ScreenState.EmptyData) shouldBe true
    }

    test("get line chart stats for a country with database problem should return loading and unknown database error") {
        val regionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsMostDeathsFlow = flow {
            delay(20)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsMostRecoveredFlow = flow {
            delay(40)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val regionStatsMostOpenCasesFlow = flow {
            delay(60)
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getRegionStats.getRegionsAndStatsWithMostConfirmed(any())).thenReturn(regionStatsMostConfirmedFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostDeaths(any())).thenReturn(regionStatsMostDeathsFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostRecovered(any())).thenReturn(regionStatsMostRecoveredFlow)
        whenever(getRegionStats.getRegionsAndStatsWithMostOpenCases(any())).thenReturn(regionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY)

        val regionStatsMostConfirmedLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostConfirmedError = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostDeathsLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostDeathsError = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostRecoveredLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostRecoveredError = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostOpenCasesLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val regionStatsMostOpenCasesError = countryViewModel.screenState.value

        regionStatsMostConfirmedLoading shouldBe ScreenState.Loading
        regionStatsMostDeathsLoading shouldBe ScreenState.Loading
        regionStatsMostRecoveredLoading shouldBe ScreenState.Loading
        regionStatsMostOpenCasesLoading shouldBe ScreenState.Loading
        (regionStatsMostConfirmedError is ScreenState.Error) shouldBe true
        ((regionStatsMostConfirmedError as ScreenState.Error)
            .errorState is PlaceStateScreen.SomeError) shouldBe true
        ((regionStatsMostConfirmedError.errorState as PlaceStateScreen.SomeError).data) shouldBe
            placeStateScreenErrorUnknownDatabase

        (regionStatsMostDeathsError is ScreenState.Error) shouldBe true
        ((regionStatsMostDeathsError as ScreenState.Error)
            .errorState is PlaceStateScreen.SomeError) shouldBe true
        ((regionStatsMostDeathsError.errorState as PlaceStateScreen.SomeError).data) shouldBe
            placeStateScreenErrorUnknownDatabase

        (regionStatsMostRecoveredError is ScreenState.Error) shouldBe true
        ((regionStatsMostRecoveredError as ScreenState.Error)
            .errorState is PlaceStateScreen.SomeError) shouldBe true
        ((regionStatsMostRecoveredError.errorState as PlaceStateScreen.SomeError).data) shouldBe
            placeStateScreenErrorUnknownDatabase

        (regionStatsMostOpenCasesError is ScreenState.Error) shouldBe true
        ((regionStatsMostOpenCasesError as ScreenState.Error)
            .errorState is PlaceStateScreen.SomeError) shouldBe true
        ((regionStatsMostOpenCasesError.errorState as PlaceStateScreen.SomeError).data) shouldBe
            placeStateScreenErrorUnknownDatabase
    }

    test("get line chart stats for a region with subregions should return loading and success") {
        val subRegionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
        }

        val subRegionStatsMostDeathsFlow = flow {
            delay(20)
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostDeathsListSubRegionAndStatsSuccess))
        }

        val subRegionStatsMostRecoveredFlow = flow {
            delay(40)
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostRecoveredListSubRegionAndStatsSuccess))
        }

        val subRegionStatsMostOpenCasesFlow = flow {
            delay(60)
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostOpenCasesListSubRegionAndStatsSuccess))
        }

        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(any(), any())).thenReturn(subRegionStatsMostConfirmedFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(any(), any())).thenReturn(subRegionStatsMostDeathsFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(any(), any())).thenReturn(subRegionStatsMostRecoveredFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(any(), any())).thenReturn(subRegionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY, ID_REGION)

        val subRegionStatsMostConfirmedLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostConfirmedSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostDeathsLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostDeathsSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostRecoveredLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostRecoveredSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostOpenCasesLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostOpenCasesSuccess = countryViewModel.screenState.value

        subRegionStatsMostConfirmedLoading shouldBe ScreenState.Loading
        subRegionStatsMostDeathsLoading shouldBe ScreenState.Loading
        subRegionStatsMostRecoveredLoading shouldBe ScreenState.Loading
        subRegionStatsMostOpenCasesLoading shouldBe ScreenState.Loading
        (subRegionStatsMostConfirmedSuccess is ScreenState.Render) shouldBe true
        ((subRegionStatsMostConfirmedSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts) shouldBe true
        ((subRegionStatsMostConfirmedSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
            .data[MenuItemViewType.LineChartMostConfirmed]) shouldBe
                stateLineChartMostConfirmedListSubRegionAndStatsSuccessData[MenuItemViewType.LineChartMostConfirmed]
        (subRegionStatsMostDeathsSuccess is ScreenState.Render) shouldBe true
        ((subRegionStatsMostDeathsSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts) shouldBe true
        ((subRegionStatsMostDeathsSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
            .data[MenuItemViewType.LineChartMostDeaths]) shouldBe
                stateLineChartMostDeathsListSubRegionAndStatsSuccessData[MenuItemViewType.LineChartMostDeaths]
        (subRegionStatsMostRecoveredSuccess is ScreenState.Render) shouldBe true
        ((subRegionStatsMostRecoveredSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts) shouldBe true
        ((subRegionStatsMostRecoveredSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
            .data[MenuItemViewType.LineChartMostRecovered]) shouldBe
                stateLineChartMostRecoveredListSubRegionAndStatsSuccessData[MenuItemViewType.LineChartMostRecovered]
        (subRegionStatsMostOpenCasesSuccess is ScreenState.Render) shouldBe true
        ((subRegionStatsMostOpenCasesSuccess as ScreenState.Render)
            .renderState is PlaceStateScreen.SuccessPlaceStatsLineCharts) shouldBe true
        ((subRegionStatsMostOpenCasesSuccess.renderState as PlaceStateScreen.SuccessPlaceStatsLineCharts)
            .data[MenuItemViewType.LineChartMostOpenCases]) shouldBe
                stateLineChartMostOpenCasesListSubRegionAndStatsSuccessData[MenuItemViewType.LineChartMostOpenCases]
    }

    test("get line chart stats for a region with empty subregions should return loading and empty data success") {
        val subRegionStatsMostConfirmedFlow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess))
        }

        val subRegionStatsMostDeathsFlow = flow {
            delay(20)
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostDeathsListSubRegionAndStatsEmptySuccess))
        }

        val subRegionStatsMostRecoveredFlow = flow {
            delay(40)
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostRecoveredListSubRegionAndStatsEmptySuccess))
        }

        val subRegionStatsMostOpenCasesFlow = flow {
            delay(60)
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            delay(10)
            emit(Either.right(stateLineChartMostOpenCasesListSubRegionAndStatsEmptySuccess))
        }

        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(any(), any())).thenReturn(subRegionStatsMostConfirmedFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(any(), any())).thenReturn(subRegionStatsMostDeathsFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(any(), any())).thenReturn(subRegionStatsMostRecoveredFlow)
        whenever(getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(any(), any())).thenReturn(subRegionStatsMostOpenCasesFlow)

        countryViewModel.getLineChartStats(ID_COUNTRY, ID_REGION)

        val subRegionStatsMostConfirmedLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostConfirmedEmptyDataSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostDeathsLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostDeathsEmptyDataSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostRecoveredLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostRecoveredEmptyDataSuccess = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostOpenCasesLoading = countryViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val subRegionStatsMostOpenCasesEmptyDataSuccess = countryViewModel.screenState.value

        subRegionStatsMostConfirmedLoading shouldBe ScreenState.Loading
        subRegionStatsMostDeathsLoading shouldBe ScreenState.Loading
        subRegionStatsMostRecoveredLoading shouldBe ScreenState.Loading
        subRegionStatsMostOpenCasesLoading shouldBe ScreenState.Loading
        (subRegionStatsMostConfirmedEmptyDataSuccess is ScreenState.EmptyData) shouldBe true
        (subRegionStatsMostDeathsEmptyDataSuccess is ScreenState.EmptyData) shouldBe true
        (subRegionStatsMostRecoveredEmptyDataSuccess is ScreenState.EmptyData) shouldBe true
        (subRegionStatsMostOpenCasesEmptyDataSuccess is ScreenState.EmptyData) shouldBe true
    }
})