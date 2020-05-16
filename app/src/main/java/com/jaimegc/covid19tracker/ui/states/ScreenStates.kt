package com.jaimegc.covid19tracker.ui.states

import com.jaimegc.covid19tracker.ui.model.*

sealed class BaseScreenState

sealed class ScreenState<out T : BaseScreenState> {
    object Loading : ScreenState<Nothing>()
    class Render<out T : BaseScreenState>(val renderState: T) : ScreenState<T>()
}

sealed class WorldStateScreen : BaseScreenState() {
    class SuccessCovidTracker(val data: CovidTrackerUI) : WorldStateScreen()
    class SuccessWorldStatsBarCharts(val data: List<WorldStatsChartUI>) : WorldStateScreen()
    class SuccessCountriesStatsBarCharts(val data: List<CountryListStatsChartUI>) : WorldStateScreen()
    class SuccessCountriesStatsLineCharts(
        val data: Map<MenuItemViewType, List<CountryListStatsChartUI>>) : WorldStateScreen()
    class SuccessCountriesStatsPieCharts(val data: List<WorldCountryStatsUI>) : WorldStateScreen()
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
    class SuccessCountryAndStats(val data: PlaceStatsUI) : PlaceStateScreen()
    class SuccessRegionStats(val data: List<PlaceStatsUI>) : PlaceStateScreen()
    class SuccessPlaceTotalStatsBarChart(val data: List<StatsChartUI>) : PlaceStateScreen()
    class SuccessPlaceStatsBarChart(val data: List<PlaceListStatsChartUI>) : PlaceStateScreen()
    class SuccessCountryAndStatsPieChart(val data: StatsChartUI) : PlaceStateScreen()
}