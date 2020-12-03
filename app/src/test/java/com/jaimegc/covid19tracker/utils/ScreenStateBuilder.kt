package com.jaimegc.covid19tracker.utils

import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListCountry
import com.jaimegc.covid19tracker.domain.model.ListCountryAndStats
import com.jaimegc.covid19tracker.domain.model.ListCountryOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegion
import com.jaimegc.covid19tracker.domain.model.ListRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListRegionOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegionStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionStats
import com.jaimegc.covid19tracker.domain.model.ListWorldStats
import com.jaimegc.covid19tracker.domain.model.RegionOneStats
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.base.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.base.states.WorldStateScreen
import com.jaimegc.covid19tracker.ui.model.toChartUI
import com.jaimegc.covid19tracker.ui.model.toListChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceUI
import com.jaimegc.covid19tracker.ui.model.toUI
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

    val stateListCountryOnlyStatsLoading: State<ListCountryOnlyStats> = State.Loading()

    val stateListRegionAndStatsLoading: State<ListRegionAndStats> = State.Loading()

    val stateMenuItemViewTypeListRegionAndStatsLoading: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.Loading()

    val stateMenuItemViewTypeListSubRegionAndStatsLoading: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.Loading()

    val stateListRegionOnlyStatsLoading: State<ListRegionOnlyStats> = State.Loading()

    val stateListSubRegionAndStatsLoading: State<ListSubRegionAndStats> = State.Loading()

    val stateCovidSuccess: State<CovidTracker> = State.Success(covidTracker)

    val stateListWorldStatsSuccess: State<ListWorldStats> = State.Success(listWorldStats)

    val stateListCountrySuccess: State<ListCountry> = State.Success(listCountry)

    val stateListRegionSuccess: State<ListRegion> = State.Success(listRegion)

    val stateCountryOneStatsSuccess: State<CountryOneStats> = State.Success(countryOneStats)

    val stateRegionOneStatsSuccess: State<RegionOneStats> = State.Success(regionOneStats)

    val stateListRegionStatsSuccess: State<ListRegionStats> = State.Success(listRegionStats)

    val stateListSubRegionStatsSuccess: State<ListSubRegionStats> = State.Success(listSubRegionStats)

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

    val stateListCountryOnlyStatsSuccess: State<ListCountryOnlyStats> =
        State.Success(listCountryOnlyStats)

    val stateListRegionAndStatsSuccess: State<ListRegionAndStats> =
        State.Success(listRegionAndStats)

    val stateLineChartMostConfirmedListRegionAndStatsSuccess: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.Success(Pair(MenuItemViewType.LineChartMostConfirmed, listRegionAndStats))

    val stateLineChartMostDeathsListRegionAndStatsSuccess: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.Success(Pair(MenuItemViewType.LineChartMostDeaths, listRegionAndStats))

    val stateLineChartMostRecoveredListRegionAndStatsSuccess: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.Success(Pair(MenuItemViewType.LineChartMostRecovered, listRegionAndStats))

    val stateLineChartMostOpenCasesListRegionAndStatsSuccess: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.Success(Pair(MenuItemViewType.LineChartMostOpenCases, listRegionAndStats))

    val stateLineChartMostConfirmedListSubRegionAndStatsSuccess: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.Success(Pair(MenuItemViewType.LineChartMostConfirmed, listSubRegionAndStats))

    val stateLineChartMostDeathsListSubRegionAndStatsSuccess: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.Success(Pair(MenuItemViewType.LineChartMostDeaths, listSubRegionAndStats))

    val stateLineChartMostRecoveredListSubRegionAndStatsSuccess: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.Success(Pair(MenuItemViewType.LineChartMostRecovered, listSubRegionAndStats))

    val stateLineChartMostOpenCasesListSubRegionAndStatsSuccess: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.Success(Pair(MenuItemViewType.LineChartMostOpenCases, listSubRegionAndStats))

    val stateListRegionOnlyStatsSuccess: State<ListRegionOnlyStats> =
        State.Success(listRegionOnlyStats)

    val stateListSubRegionAndStatsSuccess: State<ListSubRegionAndStats> =
        State.Success(listSubRegionAndStats)

    val stateListRegionEmptySuccess: State<ListRegion> =
        State.Success(ListRegion(regions = listOf()))

    val stateListRegionStatsEmptySuccess: State<ListRegionStats> =
        State.Success(ListRegionStats(regionStats = listOf()))

    val stateListSubRegionStatsEmptySuccess: State<ListSubRegionStats> =
        State.Success(ListSubRegionStats(subRegionStats = listOf()))

    val stateListRegionAndStatsEmptySuccess: State<ListRegionAndStats> =
        State.Success(ListRegionAndStats(regionStats = listOf()))

    val stateListSubRegionAndStatsEmptySuccess: State<ListSubRegionAndStats> =
        State.Success(ListSubRegionAndStats(subRegionStats = listOf()))

    val stateLineChartMostConfirmedListRegionAndStatsEmptySuccess: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.EmptyData()

    val stateLineChartMostDeathsListRegionAndStatsEmptySuccess: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.EmptyData()

    val stateLineChartMostRecoveredListRegionAndStatsEmptySuccess: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.EmptyData()

    val stateLineChartMostOpenCasesListRegionAndStatsEmptySuccess: State<Pair<MenuItemViewType, ListRegionAndStats>> =
        State.EmptyData()

    val stateLineChartMostConfirmedListSubRegionAndStatsEmptySuccess: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.EmptyData()

    val stateLineChartMostDeathsListSubRegionAndStatsEmptySuccess: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.EmptyData()

    val stateLineChartMostRecoveredListSubRegionAndStatsEmptySuccess: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.EmptyData()

    val stateLineChartMostOpenCasesListSubRegionAndStatsEmptySuccess: State<Pair<MenuItemViewType, ListSubRegionAndStats>> =
        State.EmptyData()

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

    val stateCountryOneStatsPieChartSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceTotalStatsPieChart((stateCountryOneStatsSuccess as State.Success).data.stats.toChartUI()
        )).renderState.data

    val stateRegionOneStatsSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceAndStats((stateRegionOneStatsSuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateRegionOneStatsPieChartSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceTotalStatsPieChart((stateRegionOneStatsSuccess as State.Success).data.stats.toChartUI()
        )).renderState.data

    val stateListRegionStatsSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStats((stateListRegionStatsSuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateListRegionStatsPieChartSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceAndStatsPieChart((stateListRegionStatsSuccess as State.Success).data.toPlaceChartUI()
        )).renderState.data

    val stateListSubRegionStatsSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStats((stateListSubRegionStatsSuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateListSubRegionStatsPieChartSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceAndStatsPieChart((stateListSubRegionStatsSuccess as State.Success).data.toPlaceChartUI()
        )).renderState.data

    val stateListCountryOnlyStatsBarChartSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceTotalStatsBarChart((stateListCountryOnlyStatsSuccess as State.Success).data.countriesStats.map {
                country -> country.toChartUI()
            }
        )).renderState.data

    val stateListRegionAndStatsBarChartSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStatsBarChart((stateListRegionAndStatsSuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateLineChartMostConfirmedListRegionAndStatsSuccessData =
        (stateLineChartMostConfirmedListRegionAndStatsSuccess as State.Success).let { state ->
            ScreenState.Render(
                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapOf(state.data.first to state.data.second.toPlaceUI()))
            ).renderState.data
        }

    val stateLineChartMostDeathsListRegionAndStatsSuccessData =
        (stateLineChartMostDeathsListRegionAndStatsSuccess as State.Success).let { state ->
            ScreenState.Render(
                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapOf(state.data.first to state.data.second.toPlaceUI()))
            ).renderState.data
        }

    val stateLineChartMostRecoveredListRegionAndStatsSuccessData =
        (stateLineChartMostRecoveredListRegionAndStatsSuccess as State.Success).let { state ->
            ScreenState.Render(
                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapOf(state.data.first to state.data.second.toPlaceUI()))
            ).renderState.data
        }

    val stateLineChartMostOpenCasesListRegionAndStatsSuccessData =
        (stateLineChartMostOpenCasesListRegionAndStatsSuccess as State.Success).let { state ->
            ScreenState.Render(
                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapOf(state.data.first to state.data.second.toPlaceUI()))
            ).renderState.data
        }






    val stateLineChartMostConfirmedListSubRegionAndStatsSuccessData =
        (stateLineChartMostConfirmedListSubRegionAndStatsSuccess as State.Success).let { state ->
            ScreenState.Render(
                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapOf(state.data.first to state.data.second.toPlaceUI()))
            ).renderState.data
        }

    val stateLineChartMostDeathsListSubRegionAndStatsSuccessData =
        (stateLineChartMostDeathsListSubRegionAndStatsSuccess as State.Success).let { state ->
            ScreenState.Render(
                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapOf(state.data.first to state.data.second.toPlaceUI()))
            ).renderState.data
        }

    val stateLineChartMostRecoveredListSubRegionAndStatsSuccessData =
        (stateLineChartMostRecoveredListSubRegionAndStatsSuccess as State.Success).let { state ->
            ScreenState.Render(
                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapOf(state.data.first to state.data.second.toPlaceUI()))
            ).renderState.data
        }

    val stateLineChartMostOpenCasesListSubRegionAndStatsSuccessData =
        (stateLineChartMostOpenCasesListSubRegionAndStatsSuccess as State.Success).let { state ->
            ScreenState.Render(
                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapOf(state.data.first to state.data.second.toPlaceUI()))
            ).renderState.data
        }

    val stateListRegionOnlyStatsBarChartSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceTotalStatsBarChart((stateListRegionOnlyStatsSuccess as State.Success).data.regionStats.map {
                region -> region.toChartUI()
            }
        )).renderState.data

    val stateListSubRegionAndStatsBarChartSuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStatsBarChart((stateListSubRegionAndStatsSuccess as State.Success).data.toPlaceUI()
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

    val stateScreenListRegionStatsPieChartEmptySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceAndStatsPieChart((stateListRegionStatsEmptySuccess as State.Success).data.toPlaceChartUI()
        )).renderState.data

    val stateScreenListSubRegionStatsEmptySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStats((stateListSubRegionStatsEmptySuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateScreenListSubRegionStatsPieChartEmptySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceAndStatsPieChart((stateListSubRegionStatsEmptySuccess as State.Success).data.toPlaceChartUI()
        )).renderState.data

    val stateScreenListSubRegionStatsBarChartEmptySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStatsBarChart((stateListRegionAndStatsEmptySuccess as State.Success).data.toPlaceUI()
        )).renderState.data

    val stateListSubRegionAndStatsBarChartEmptySuccessData =
        ScreenState.Render(
            PlaceStateScreen.SuccessPlaceStatsBarChart((stateListSubRegionAndStatsEmptySuccess as State.Success).data.toPlaceUI()
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