package com.jaimegc.covid19tracker.viewmodel.kotest

import arrow.core.Either
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetWorldAndCountries
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.base.states.WorldStateScreen
import com.jaimegc.covid19tracker.ui.world.WorldViewModel
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostConfirmedSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostDeathsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostOpenCasesSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostRecoveredSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListWorldStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListWorldStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListWorldStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.worldStateScreenErrorUnknownDatatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenSuccessCountriesStatsPieChartData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenSuccessCovidTrackerData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenSuccessListCountryAndStatsBarChartData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenSuccessListCountryAndStatsLineChartMostConfirmedData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenSuccessListCountryAndStatsLineChartMostDeathsData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenSuccessListCountryAndStatsLineChartMostOpenCasesData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenSuccessListCountryAndStatsLineChartMostRecoveredData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateScreenSuccessListWorldStatsPieChartData
import com.jaimegc.covid19tracker.util.kotest.ProjectConfig
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class WorldViewModelKotestTest : FunSpec({

    lateinit var worldViewModel: WorldViewModel
    val getWorldAndCountries: GetWorldAndCountries = mock()
    val getWorldStats: GetWorldStats = mock()
    val getCountryStats: GetCountryStats = mock()

    beforeTest {
        worldViewModel = WorldViewModel(getWorldAndCountries, getWorldStats, getCountryStats)
    }

    test("get list stats should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerSuccess))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getListStats()

        val loading = worldViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val success = worldViewModel.screenState.value
        
        loading shouldBe ScreenState.Loading
        (success is ScreenState.Render) shouldBe true
        ((success as ScreenState.Render).renderState is WorldStateScreen.SuccessCovidTracker) shouldBe true
        (success.renderState as WorldStateScreen.SuccessCovidTracker).data shouldBe
            stateScreenSuccessCovidTrackerData
    }

    test("get list stats with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getListStats()

        val loading = worldViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val empty = worldViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        empty shouldBe ScreenState.EmptyData
    }

    test("get list stats with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getListStats()

        val loading = worldViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val error = worldViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        (error is ScreenState.Error) shouldBe true
        ((error as ScreenState.Error).errorState is WorldStateScreen.SomeError) shouldBe true
        (error.errorState as WorldStateScreen.SomeError).data shouldBe
            worldStateScreenErrorUnknownDatatabase
    }

    test("get pie chart stats should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerSuccess))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getPieChartStats()

        val loading = worldViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val success = worldViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        (success is ScreenState.Render) shouldBe true
        ((success as ScreenState.Render)
            .renderState is WorldStateScreen.SuccessCountriesStatsPieCharts) shouldBe true
        (success.renderState as WorldStateScreen.SuccessCountriesStatsPieCharts).data shouldBe
            stateScreenSuccessCountriesStatsPieChartData
    }

    test("get pie chart stats with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getPieChartStats()

        val loading = worldViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val empty = worldViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        empty shouldBe ScreenState.EmptyData
    }

    test("get pie chart stats with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getPieChartStats()

        val loading = worldViewModel.screenState.value
        ProjectConfig.advanceUntilIdle()
        val error = worldViewModel.screenState.value

        loading shouldBe ScreenState.Loading
        (error is ScreenState.Error) shouldBe true
        ((error as ScreenState.Error).errorState is WorldStateScreen.SomeError) shouldBe true
        (error.errorState as WorldStateScreen.SomeError).data shouldBe
            worldStateScreenErrorUnknownDatatabase
    }

    test("get bar chart stats should return loading and success if date exists") {
        val worldFlow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            delay(10)
            emit(Either.right(stateListWorldStatsSuccess))
        }

        val countriesFlow = flow {
            delay(20)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsSuccess))
        }

        whenever(getWorldStats.getWorldAllStats()).thenReturn(worldFlow)
        whenever(getCountryStats.getCountriesStatsOrderByConfirmed()).thenReturn(countriesFlow)

        worldViewModel.getBarChartStats()

        val worldLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val worldSuccess = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val countriesLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val countriesSuccess = worldViewModel.screenState.value

        worldLoading shouldBe ScreenState.Loading
        countriesLoading shouldBe ScreenState.Loading
        (worldSuccess is ScreenState.Render) shouldBe true
        ((worldSuccess as ScreenState.Render)
            .renderState is WorldStateScreen.SuccessWorldStatsBarCharts) shouldBe true
        (worldSuccess.renderState as WorldStateScreen.SuccessWorldStatsBarCharts).data shouldBe
            stateScreenSuccessListWorldStatsPieChartData
        (countriesSuccess is ScreenState.Render) shouldBe true
        ((countriesSuccess as ScreenState.Render)
            .renderState is WorldStateScreen.SuccessCountriesStatsBarCharts) shouldBe true
        (countriesSuccess.renderState as WorldStateScreen.SuccessCountriesStatsBarCharts).data shouldBe
            stateScreenSuccessListCountryAndStatsBarChartData
    }

    test("get bar chart stats with empty data should return loading and empty success") {
        val worldFlow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            delay(10)
            emit(Either.right(stateListWorldStatsEmptyData))
        }

        val countriesFlow = flow {
            delay(20)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        whenever(getWorldStats.getWorldAllStats()).thenReturn(worldFlow)
        whenever(getCountryStats.getCountriesStatsOrderByConfirmed()).thenReturn(countriesFlow)

        worldViewModel.getBarChartStats()

        val worldLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val worldEmpty = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val countriesLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val countriesEmpty = worldViewModel.screenState.value

        worldLoading shouldBe ScreenState.Loading
        countriesLoading shouldBe ScreenState.Loading
        worldEmpty shouldBe ScreenState.EmptyData
        countriesEmpty shouldBe ScreenState.EmptyData
    }

    test("get bar chart stats with database problem should return loading and unknown database error") {
        val worldFlow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val countriesFlow = flow {
            delay(20)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getWorldStats.getWorldAllStats()).thenReturn(worldFlow)
        whenever(getCountryStats.getCountriesStatsOrderByConfirmed()).thenReturn(countriesFlow)

        worldViewModel.getBarChartStats()

        val worldLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val worldError = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val countriesLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val countriesError = worldViewModel.screenState.value

        worldLoading shouldBe ScreenState.Loading
        countriesLoading shouldBe ScreenState.Loading
        (worldError is ScreenState.Error) shouldBe true
        ((worldError as ScreenState.Error).errorState is WorldStateScreen.SomeError) shouldBe true
        (worldError.errorState as WorldStateScreen.SomeError).data shouldBe
            worldStateScreenErrorUnknownDatatabase
        (countriesError is ScreenState.Error) shouldBe true
        ((countriesError as ScreenState.Error).errorState is WorldStateScreen.SomeError) shouldBe true
        (countriesError.errorState as WorldStateScreen.SomeError).data shouldBe
            worldStateScreenErrorUnknownDatatabase
    }

    test("get line charts stats should return loading and success if date exists") {
        val mostConfirmedFlow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsMostConfirmedSuccess))
        }

        val mostDeathsFlow = flow {
            delay(20)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsMostDeathsSuccess))
        }

        val mostOpenCasesFlow = flow {
            delay(40)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsMostOpenCasesSuccess))
        }

        val mostRecoveredFlow = flow {
            delay(60)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsMostRecoveredSuccess))
        }

        whenever(getCountryStats.getCountriesAndStatsWithMostConfirmed()).thenReturn(mostConfirmedFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostDeaths()).thenReturn(mostDeathsFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostOpenCases()).thenReturn(mostOpenCasesFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostRecovered()).thenReturn(mostRecoveredFlow)

        worldViewModel.getLineChartsStats()

        val mostConfirmedLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostConfirmedSuccess = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostDeathsLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostDeathsSuccess = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostOpenCasesLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostOpenCasesSuccess = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostRecoveredLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostRecoveredSuccess = worldViewModel.screenState.value

        mostConfirmedLoading shouldBe ScreenState.Loading
        mostDeathsLoading shouldBe ScreenState.Loading
        mostOpenCasesLoading shouldBe ScreenState.Loading
        mostRecoveredLoading shouldBe ScreenState.Loading

        (mostConfirmedSuccess is ScreenState.Render) shouldBe true
        ((mostConfirmedSuccess as ScreenState.Render)
            .renderState is WorldStateScreen.SuccessCountriesStatsLineCharts) shouldBe true
        (mostConfirmedSuccess.renderState as WorldStateScreen.SuccessCountriesStatsLineCharts)
            .data[MenuItemViewType.LineChartMostConfirmed] shouldBe
                stateScreenSuccessListCountryAndStatsLineChartMostConfirmedData[MenuItemViewType.LineChartMostConfirmed]
        (mostDeathsSuccess is ScreenState.Render) shouldBe true
        ((mostDeathsSuccess as ScreenState.Render)
            .renderState is WorldStateScreen.SuccessCountriesStatsLineCharts) shouldBe true

        (mostDeathsSuccess.renderState as WorldStateScreen.SuccessCountriesStatsLineCharts)
            .data[MenuItemViewType.LineChartMostDeaths] shouldBe
                stateScreenSuccessListCountryAndStatsLineChartMostDeathsData[MenuItemViewType.LineChartMostDeaths]
        (mostOpenCasesSuccess is ScreenState.Render) shouldBe true
        ((mostOpenCasesSuccess as ScreenState.Render)
            .renderState is WorldStateScreen.SuccessCountriesStatsLineCharts) shouldBe true
        (mostOpenCasesSuccess.renderState as WorldStateScreen.SuccessCountriesStatsLineCharts)
            .data[MenuItemViewType.LineChartMostOpenCases] shouldBe
                stateScreenSuccessListCountryAndStatsLineChartMostOpenCasesData[MenuItemViewType.LineChartMostOpenCases]
        (mostRecoveredSuccess is ScreenState.Render) shouldBe true
        ((mostRecoveredSuccess as ScreenState.Render)
            .renderState is WorldStateScreen.SuccessCountriesStatsLineCharts) shouldBe true
        (mostRecoveredSuccess.renderState as WorldStateScreen.SuccessCountriesStatsLineCharts)
            .data[MenuItemViewType.LineChartMostRecovered] shouldBe
                stateScreenSuccessListCountryAndStatsLineChartMostRecoveredData[MenuItemViewType.LineChartMostRecovered]
    }

    test("get line charts stats with empty data should return loading and empty success") {
        val mostConfirmedFlow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        val mostDeathsFlow = flow {
            delay(20)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        val mostOpenCasesFlow = flow {
            delay(40)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        val mostRecoveredFlow = flow {
            delay(60)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        whenever(getCountryStats.getCountriesAndStatsWithMostConfirmed()).thenReturn(mostConfirmedFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostDeaths()).thenReturn(mostDeathsFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostOpenCases()).thenReturn(mostOpenCasesFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostRecovered()).thenReturn(mostRecoveredFlow)

        worldViewModel.getLineChartsStats()

        val mostConfirmedLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostConfirmedEmpty = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostDeathsLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostDeathsEmpty = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostOpenCasesLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostOpenCasesEmpty = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostRecoveredLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostRecoveredEmpty = worldViewModel.screenState.value

        mostConfirmedLoading shouldBe ScreenState.Loading
        mostDeathsLoading shouldBe ScreenState.Loading
        mostOpenCasesLoading shouldBe ScreenState.Loading
        mostRecoveredLoading shouldBe ScreenState.Loading
        mostConfirmedEmpty shouldBe ScreenState.EmptyData
        mostDeathsEmpty shouldBe ScreenState.EmptyData
        mostOpenCasesEmpty shouldBe ScreenState.EmptyData
        mostRecoveredEmpty shouldBe ScreenState.EmptyData
    }

    test("get line charts stats with database problem should return loading and unknown database error") {
        val mostConfirmedFlow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val mostDeathsFlow = flow {
            delay(20)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val mostOpenCasesFlow = flow {
            delay(40)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        val mostRecoveredFlow = flow {
            delay(60)
            emit(Either.right(stateListCountryAndStatsLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getCountryStats.getCountriesAndStatsWithMostConfirmed()).thenReturn(mostConfirmedFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostDeaths()).thenReturn(mostDeathsFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostOpenCases()).thenReturn(mostOpenCasesFlow)
        whenever(getCountryStats.getCountriesAndStatsWithMostRecovered()).thenReturn(mostRecoveredFlow)

        worldViewModel.getLineChartsStats()

        val mostConfirmedLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostConfirmedError = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostDeathsLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostDeathsError = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostOpenCasesLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostOpenCasesError = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostRecoveredLoading = worldViewModel.screenState.value
        ProjectConfig.advanceTimeBy(10)
        val mostRecoveredError = worldViewModel.screenState.value

        mostConfirmedLoading shouldBe ScreenState.Loading
        mostDeathsLoading shouldBe ScreenState.Loading
        mostOpenCasesLoading shouldBe ScreenState.Loading
        mostRecoveredLoading shouldBe ScreenState.Loading
        (mostConfirmedError is ScreenState.Error) shouldBe true
        ((mostConfirmedError as ScreenState.Error).errorState is WorldStateScreen.SomeError) shouldBe true
        (mostConfirmedError.errorState as WorldStateScreen.SomeError).data shouldBe
            worldStateScreenErrorUnknownDatatabase
        (mostDeathsError is ScreenState.Error) shouldBe true
        ((mostDeathsError as ScreenState.Error).errorState is WorldStateScreen.SomeError) shouldBe true
        (mostDeathsError.errorState as WorldStateScreen.SomeError).data shouldBe
            worldStateScreenErrorUnknownDatatabase
        (mostOpenCasesError is ScreenState.Error) shouldBe true
        ((mostOpenCasesError as ScreenState.Error).errorState is WorldStateScreen.SomeError) shouldBe true
        (mostOpenCasesError.errorState as WorldStateScreen.SomeError).data shouldBe
            worldStateScreenErrorUnknownDatatabase
        (mostRecoveredError is ScreenState.Error) shouldBe true
        ((mostRecoveredError as ScreenState.Error).errorState is WorldStateScreen.SomeError) shouldBe true
        (mostRecoveredError.errorState as WorldStateScreen.SomeError).data shouldBe
            worldStateScreenErrorUnknownDatatabase
    }
})