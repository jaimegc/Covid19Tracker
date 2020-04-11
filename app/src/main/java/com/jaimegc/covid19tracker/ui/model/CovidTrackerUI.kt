package com.jaimegc.covid19tracker.ui.model

data class CovidTrackerUI(
    val total: CovidTrackerTotalUI
)

data class CovidTrackerTotalUI(
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
    val todayVsYesterdayRecovered: String
)

