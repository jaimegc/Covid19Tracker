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
        val data: Map<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>) : WorldStateScreen()
    class SuccessCountriesStatsPieCharts(val data: List<WorldCountryStatsUI>) : WorldStateScreen()
}

sealed class CovidTrackerType {
    object Normal : CovidTrackerType()
    object PieChart : CovidTrackerType()
}

sealed class WorldStateCountriesStatsLineChartType {
    object MostConfirmed : WorldStateCountriesStatsLineChartType()
    object MostDeaths : WorldStateCountriesStatsLineChartType()
    object MostRecovered : WorldStateCountriesStatsLineChartType()
    object MostOpenCases : WorldStateCountriesStatsLineChartType()
}

sealed class CountryStateScreen : BaseScreenState() {
    class SuccessCountries(val data: List<CountryUI>) : CountryStateScreen()
}