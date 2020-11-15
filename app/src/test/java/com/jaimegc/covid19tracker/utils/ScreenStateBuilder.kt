package com.jaimegc.covid19tracker.utils

import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListCountry
import com.jaimegc.covid19tracker.domain.model.ListCountryAndStats
import com.jaimegc.covid19tracker.domain.model.ListRegion
import com.jaimegc.covid19tracker.domain.model.ListRegionStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionStats
import com.jaimegc.covid19tracker.domain.model.ListWorldStats
import com.jaimegc.covid19tracker.domain.model.RegionOneStats
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.base.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.base.states.WorldStateScreen
import com.jaimegc.covid19tracker.ui.model.toListChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceUI
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryOneStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTracker
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountry
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegion
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listWorldStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionOneStats

object ScreenStateBuilder {
    val stateCovidTrackerLoading: State<CovidTracker> = State.Loading()

    val stateListWorldStatsLoading: State<ListWorldStats> = State.Loading()

    val stateListCountryAndStatsLoading: State<ListCountryAndStats> = State.Loading()

    val stateListCountryLoading: State<ListCountry> = State.Loading()

    val stateListRegionLoading: State<ListRegion> = State.Loading()

    val stateCountryOneStatsLoading: State<CountryOneStats> = State.Loading()

    val stateRegionOneStatsLoading: State<RegionOneStats> = State.Loading()

    val stateListRegionStatsLoading: State<ListRegionStats> = State.Loading()

    val stateListSubRegionStatsLoading: State<ListSubRegionStats> = State.Loading()

    val stateCovidSuccess: State<CovidTracker> = State.Success(covidTracker)

    val stateListWorldStatsSuccess: State<ListWorldStats> = State.Success(listWorldStats)

    val stateListCountrySuccess: State<ListCountry> = State.Success(listCountry)

    val stateListRegionSuccess: State<ListRegion> = State.Success(listRegion)

    val stateCountryOneStatsSuccess: State<CountryOneStats> = State.Success(countryOneStats)

    val stateRegionOneStatsSuccess: State<RegionOneStats> = State.Success(regionOneStats)

    val stateListRegionStatsSuccess: State<ListRegionStats> = State.Success(listRegionStats)

    val stateListSubRegionStatsSuccess: State<ListSubRegionStats> = State.Success(listSubRegionStats)

    val stateListRegionEmptySuccess: State<ListRegion> =
        State.Success(ListRegion(regions = listOf()))

    val stateListRegionStatsEmptySuccess: State<ListRegionStats> =
        State.Success(ListRegionStats(regionStats = listOf()))

    val stateListSubRegionStatsEmptySuccess: State<ListSubRegionStats> =
        State.Success(ListSubRegionStats(subRegionStats = listOf()))

    val stateListCountryAndStatsSuccess: State<ListCountryAndStats> =
        State.Success(listCountryAndStats)

    val stateListCountryAndStatsMostConfirmedSuccess: State<ListCountryAndStats> =
        State.Success(listCountryAndStats.copy(countriesStats = listOf(
            listCountryAndStats.countriesStats[0].copy(country =
            listCountryAndStats.countriesStats[0].country.copy(code = "most_confirmed"))))
        )

    val stateListCountryAndStatsMostDeathsSuccess: State<ListCountryAndStats> =
        State.Success(listCountryAndStats.copy(countriesStats = listOf(
            listCountryAndStats.countriesStats[0].copy(country =
            listCountryAndStats.countriesStats[0].country.copy(code = "most_deaths"))))
        )

    val stateListCountryAndStatsMostOpenCasesSuccess: State<ListCountryAndStats> =
        State.Success(listCountryAndStats.copy(countriesStats = listOf(
            listCountryAndStats.countriesStats[0].copy(country =
            listCountryAndStats.countriesStats[0].country.copy(code = "most_open_cases"))))
        )

    val stateListCountryAndStatsMostRecoveredSuccess: State<ListCountryAndStats> =
        State.Success(listCountryAndStats.copy(countriesStats = listOf(
            listCountryAndStats.countriesStats[0].copy(country =
            listCountryAndStats.countriesStats[0].country.copy(code = "most_recovered"))))
        )

    val stateErrorDatabaseEmpty: StateError<DomainError> =
        StateError.Error(DomainError.DatabaseEmptyData)

    val stateScreenSuccessCovidTrackerData =
        ScreenState.Render(
            WorldStateScreen.SuccessCovidTracker((stateCovidSuccess as State.Success).data.toUI())
        ).renderState.data

    val stateScreenSuccessCountriesStatsPieChartData =
        ScreenState.Render(
            WorldStateScreen.SuccessCountriesStatsPieCharts(
                (stateCovidSuccess as State.Success).data.toListChartUI())
        ).renderState.data

    val stateScreenSuccessListWorldStatsPieChartData =
        ScreenState.Render(
            WorldStateScreen.SuccessWorldStatsBarCharts(
                (stateListWorldStatsSuccess as State.Success).data.worldStats.map {
                    worldStats -> worldStats.toListChartUI()
                }
            )
        ).renderState.data

    val stateScreenSuccessListCountryAndStatsBarChartData =
        ScreenState.Render(
            WorldStateScreen.SuccessCountriesStatsBarCharts(
                (stateListCountryAndStatsSuccess as State.Success).data.countriesStats.map {
                    countryStats -> countryStats.toListChartUI()
                }
            )
        ).renderState.data

    val stateScreenSuccessListCountryAndStatsLineChartMostConfirmedData =
        ScreenState.Render(
            WorldStateScreen.SuccessCountriesStatsLineCharts(
                mapOf(MenuItemViewType.LineChartMostConfirmed to
                    (stateListCountryAndStatsMostConfirmedSuccess as State.Success).data.countriesStats.map {
                        countryStats -> countryStats.toListChartUI()
                    }
                )
            )
        ).renderState.data

    val stateScreenSuccessListCountryAndStatsLineChartMostDeathsData =
        ScreenState.Render(
            WorldStateScreen.SuccessCountriesStatsLineCharts(
                mapOf(MenuItemViewType.LineChartMostDeaths to
                    (stateListCountryAndStatsMostDeathsSuccess as State.Success).data.countriesStats.map {
                        countryStats -> countryStats.toListChartUI()
                    }
                )
            )
        ).renderState.data

    val stateScreenSuccessListCountryAndStatsLineChartMostOpenCasesData =
        ScreenState.Render(
            WorldStateScreen.SuccessCountriesStatsLineCharts(
                mapOf(MenuItemViewType.LineChartMostOpenCases to
                    (stateListCountryAndStatsMostOpenCasesSuccess as State.Success).data.countriesStats.map {
                        countryStats -> countryStats.toListChartUI()
                    }
                )
            )
        ).renderState.data

    val stateScreenSuccessListCountryAndStatsLineChartMostRecoveredData =
        ScreenState.Render(
            WorldStateScreen.SuccessCountriesStatsLineCharts(
                mapOf(MenuItemViewType.LineChartMostRecovered to
                    (stateListCountryAndStatsMostRecoveredSuccess as State.Success).data.countriesStats.map {
                         countryStats -> countryStats.toListChartUI()
                    }
                )
            )
        ).renderState.data

    val stateScreenListCountrySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessSpinnerCountries((stateListCountrySuccess as State.Success).data.countries.map {
                country -> country.toUI()
            })
        ).renderState.data

    val stateScreenListRegionSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessSpinnerRegions((stateListRegionSuccess as State.Success).data.regions.map {
                region -> region.toPlaceUI()
            })
        ).renderState.data

    val stateCountryOneStatsSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceAndStats((stateCountryOneStatsSuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateRegionOneStatsSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceAndStats((stateRegionOneStatsSuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateListRegionStatsSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStats((stateListRegionStatsSuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateListSubRegionStatsSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStats((stateListSubRegionStatsSuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateScreenListRegionEmptySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessSpinnerRegions((stateListRegionEmptySuccess as State.Success).data.regions.map {
                region -> region.toPlaceUI()
            })
        ).renderState.data

    val stateScreenListRegionStatsEmptySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStats((stateListRegionStatsEmptySuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateScreenListSubRegionStatsEmptySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStats((stateListSubRegionStatsEmptySuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val worldStateScreenErrorDatabaseEmptyData =
        ScreenState.Error(
            WorldStateScreen.SomeError((stateErrorDatabaseEmpty as StateError.Error).error.toUI())
        ).errorState.data

    val placeStateScreenErrorDatabaseEmptyData =
        ScreenState.Error(
            PlaceStateScreen.SomeError((stateErrorDatabaseEmpty as StateError.Error).error.toUI())
        ).errorState.data
}