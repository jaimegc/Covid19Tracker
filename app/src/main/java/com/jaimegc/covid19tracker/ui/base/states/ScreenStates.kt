package com.jaimegc.covid19tracker.ui.base.states

import com.jaimegc.covid19tracker.ui.model.*

sealed class BaseScreenState

sealed class ScreenState<out T : BaseScreenState> {
    object Loading : ScreenState<Nothing>()
    object EmptyData : ScreenState<Nothing>()
    class Error<out T : BaseScreenState>(val errorState: T) : ScreenState<T>()
    class Render<out T : BaseScreenState>(val renderState: T) : ScreenState<T>()
}

sealed class WorldStateScreen : BaseScreenState() {
    class SuccessCovidTracker(val data: CovidTrackerUI) : WorldStateScreen()
    class SuccessWorldStatsBarCharts(val data: List<WorldStatsChartUI>) : WorldStateScreen()
    class SuccessCountriesStatsBarCharts(val data: List<CountryListStatsChartUI>) : WorldStateScreen()
    class SuccessCountriesStatsLineCharts(
        val data: Map<MenuItemViewType, List<CountryListStatsChartUI>>) : WorldStateScreen()
    class SuccessCountriesStatsPieCharts(val data: List<WorldCountryStatsUI>) : WorldStateScreen()
    class SomeError(val data: ErrorUI) : WorldStateScreen()
}

sealed class MenuItemViewType {
    object List : MenuItemViewType()
    object BarChart : MenuItemViewType()
    object LineChartMostConfirmed : MenuItemViewType()
    object LineChartMostDeaths : MenuItemViewType()
    object LineChartMostRecovered : MenuItemViewType()
    object LineChartMostOpenCases : MenuItemViewType()
    object PieChart : MenuItemViewType()
}

sealed class PlaceStateScreen : BaseScreenState() {
    class SuccessSpinnerCountries(val data: List<CountryUI>) : PlaceStateScreen()
    class SuccessSpinnerRegions(val data: List<PlaceUI>) : PlaceStateScreen()
    class SuccessPlaceAndStats(val data: PlaceStatsUI) : PlaceStateScreen()
    class SuccessPlaceStats(val data: List<PlaceStatsUI>) : PlaceStateScreen()
    class SuccessPlaceTotalStatsBarChart(val data: List<StatsChartUI>) : PlaceStateScreen()
    class SuccessPlaceStatsBarChart(val data: List<PlaceListStatsChartUI>) : PlaceStateScreen()
    class SuccessPlaceTotalStatsPieChart(val data: StatsChartUI) : PlaceStateScreen()
    class SuccessPlaceAndStatsPieChart(val data: List<PlaceStatsChartUI>) : PlaceStateScreen()
    class SuccessPlaceStatsLineCharts(
        val data: Map<MenuItemViewType, List<PlaceListStatsChartUI>>) : PlaceStateScreen()
    class SomeError(val data: ErrorUI) : PlaceStateScreen()
}