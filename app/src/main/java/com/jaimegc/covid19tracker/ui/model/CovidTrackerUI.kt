package com.jaimegc.covid19tracker.ui.model

data class CovidTrackerUI(
    val countriesStats: List<CountryStatsUI>,
    val worldStats: WorldStatsUI
)

data class WorldStatsUI(
    val date: String,
    val updatedAt: String,
    val stats: StatsUI
)

data class CountryStatsUI(
    val id: String,
    val name: String,
    val nameEs: String,
    val stats: StatsUI,
    var isExpanded: Boolean = false
)

data class StatsUI(
    val date: String,
    val source: String,
    val confirmed: String,
    val deaths: String,
    val newConfirmed: String,
    val newDeaths: String,
    val newOpenCases: String,
    val newRecovered: String,
    val openCases: String,
    val recovered: String,
    val vsYesterdayConfirmed: String,
    val vsYesterdayDeaths: String,
    val vsYesterdayOpenCases: String,
    val vsYesterdayRecovered: String
)

data class WorldStatsChartUI(
    val date: String,
    val updatedAt: String,
    val stats: StatsChartUI
)

data class StatsChartUI(
    val date: String,
    val source: String,
    val confirmed: Float,
    val deaths: Float,
    val newConfirmed: Float,
    val newDeaths: Float,
    val newOpenCases: Float,
    val newRecovered: Float,
    val openCases: Float,
    val recovered: Float
)



