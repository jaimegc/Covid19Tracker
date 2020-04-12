package com.jaimegc.covid19tracker.ui.model

data class CovidTrackerUI(
    val countryStats: CovidTrackerCountryStatsUI,
    val worldStats: CovidTrackerWorldStatsUI
)

data class CovidTrackerCountryStatsUI(
    val countries: List<CovidTrackerCountryUI>
)

data class CovidTrackerCountryUI(
    val id: String,
    val name: String,
    val nameEs: String,
    val date: String,
    val worldStats: CovidTrackerWorldStatsUI,
    var isExpanded: Boolean = false
)

data class CovidTrackerWorldStatsUI(
    val date: String,
    val source: String,
    val todayConfirmed: String,
    val todayDeaths: String,
    val todayNewConfirmed: String,
    val todayNewDeaths: String,
    val todayNewOpenCases: String,
    val todayNewRecovered: String,
    val todayOpenCases: String,
    val todayRecovered: String,
    val todayVsYesterdayConfirmed: String,
    val todayVsYesterdayDeaths: String,
    val todayVsYesterdayOpenCases: String,
    val todayVsYesterdayRecovered: String,
    val updatedAt: String
)

