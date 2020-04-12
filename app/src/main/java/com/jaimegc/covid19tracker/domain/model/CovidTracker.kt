package com.jaimegc.covid19tracker.domain.model

import com.jaimegc.covid19tracker.extensions.formatDecimals
import com.jaimegc.covid19tracker.extensions.formatValue
import com.jaimegc.covid19tracker.ui.model.*

data class CovidTracker(
    val date: String,
    val countryStats: CovidTrackerCountryStats,
    val worldStats: CovidTrackerWorldStats
)

data class CovidTrackerCountryStats(
    val countries: List<CovidTrackerCountry>
)

data class CovidTrackerCountry(
    val id: String,
    val name: String,
    val nameEs: String,
    val date: String,
    val worldStats: CovidTrackerWorldStats
)

data class CovidTrackerWorldStats(
    val date: String,
    val source: String,
    val todayConfirmed: Long,
    val todayDeaths: Long,
    val todayNewConfirmed: Long,
    val todayNewDeaths: Long,
    val todayNewOpenCases: Long,
    val todayNewRecovered: Long,
    val todayOpenCases: Long,
    val todayRecovered: Long,
    val todayVsYesterdayConfirmed: Double,
    val todayVsYesterdayDeaths: Double,
    val todayVsYesterdayOpenCases: Double,
    val todayVsYesterdayRecovered: Double,
    val updatedAt: String
)

fun CovidTracker.toUI(): CovidTrackerUI =
    CovidTrackerUI(
        countryStats = countryStats.toUI(),
        worldStats = worldStats.toUI()
    )

fun CovidTrackerCountryStats.toUI(): CovidTrackerCountryStatsUI =
    CovidTrackerCountryStatsUI(
        countries = countries.map { country -> country.toUI() }.toList()
    )

private fun CovidTrackerCountry.toUI():  CovidTrackerCountryUI =
    CovidTrackerCountryUI(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        worldStats = worldStats.toUI()
    )

fun CovidTrackerWorldStats.toUI(): CovidTrackerWorldStatsUI =
    CovidTrackerWorldStatsUI(
        date = date,
        source = source,
        todayConfirmed = todayConfirmed.formatValue(),
        todayDeaths = todayDeaths.formatValue(),
        todayNewConfirmed = todayNewConfirmed.formatValue(),
        todayNewDeaths = todayNewDeaths.formatValue(),
        todayNewOpenCases = todayNewOpenCases.formatValue(),
        todayNewRecovered = todayNewRecovered.formatValue(),
        todayOpenCases = todayOpenCases.formatValue(),
        todayRecovered = todayRecovered.formatValue(),
        todayVsYesterdayConfirmed = (todayVsYesterdayConfirmed * 100).formatDecimals(),
        todayVsYesterdayDeaths = (todayVsYesterdayDeaths * 100).formatDecimals(),
        todayVsYesterdayOpenCases = (todayVsYesterdayOpenCases * 100).formatDecimals(),
        todayVsYesterdayRecovered = (todayVsYesterdayRecovered * 100).formatDecimals(),
        updatedAt = updatedAt
    )