package com.jaimegc.covid19tracker.ui.model

data class CovidTrackerUI(
    val countryStats: CountryStatsUI,
    val worldStats: TodayStatsUI
)

data class CountryStatsUI(
    val countries: List<CountryUI>
)

data class CountryUI(
    val id: String,
    val name: String,
    val nameEs: String,
    val date: String,
    val todayStats: TodayStatsUI,
    var isExpanded: Boolean = false
)

data class TodayStatsUI(
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
    val vsYesterdayRecovered: String,
    val updatedAt: String
)

