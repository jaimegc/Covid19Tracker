package com.jaimegc.covid19tracker.domain.model

data class CovidTracker(
    val date: String,
    val updatedAt: String,
    val countryStats: CountryStats,
    val worldStats: TodayStats
)

data class CountryStats(
    val countries: List<Country>
)

data class Country(
    val id: String,
    val name: String,
    val nameEs: String,
    val date: String,
    val todayStats: TodayStats
)

data class TodayStats(
    val date: String,
    val source: String,
    val confirmed: Long,
    val deaths: Long,
    val newConfirmed: Long,
    val newDeaths: Long,
    val newOpenCases: Long,
    val newRecovered: Long,
    val openCases: Long,
    val recovered: Long,
    val vsYesterdayConfirmed: Double,
    val vsYesterdayDeaths: Double,
    val vsYesterdayOpenCases: Double,
    val vsYesterdayRecovered: Double,
    val updatedAt: String
)

