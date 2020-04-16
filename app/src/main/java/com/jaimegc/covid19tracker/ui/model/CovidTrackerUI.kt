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
    val date: String,
    val stats: StatsUI,
    var isExpanded: Boolean = false
)

data class StatsUI(
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



