package com.jaimegc.covid19tracker.domain.model

import com.jaimegc.covid19tracker.extensions.formatDecimals
import com.jaimegc.covid19tracker.extensions.formatValue
import com.jaimegc.covid19tracker.ui.model.*

data class CovidTracker(
    val dates: Map<String, CovidTrackerDate>,
    val total: CovidTrackerTotal
)

data class CovidTrackerDate(
    val countries: Map<String, CovidTrackerDateCountry>,
    val date: CovidTrackerDateInfo
)

data class CovidTrackerDateCountry(
    val id: String,
    val name: String,
    val nameEs: String,
    val total: CovidTrackerTotal
)

data class CovidTrackerDateInfo(
    val date: String,
    val dateGeneration: String,
    val yesterday: String
)

data class CovidTrackerTotal(
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
    val todayVsYesterdayRecovered: Double
)

fun CovidTracker.toUI(): CovidTrackerUI =
    CovidTrackerUI(
        dates = dates.map { date -> date.value.toUI(date.key) }.toList(),
        total = total.toUI()
    )

fun CovidTrackerDate.toUI(date: String): CovidTrackerDateUI =
    CovidTrackerDateUI(
        date = date,
        countries = countries.map { country -> country.value.toUI() }.toList()
    )

private fun CovidTrackerDateCountry.toUI():  CovidTrackerDateCountryUI =
    CovidTrackerDateCountryUI(
        id = id,
        name = name,
        nameEs = nameEs,
        total = total.toUI()
    )

fun CovidTrackerTotal.toUI(): CovidTrackerTotalUI =
    CovidTrackerTotalUI(
        date = this.date,
        source = this.source,
        todayConfirmed = this.todayConfirmed.formatValue(),
        todayDeaths = this.todayDeaths.formatValue(),
        todayNewConfirmed = this.todayNewConfirmed.formatValue(),
        todayNewDeaths = this.todayNewDeaths.formatValue(),
        todayNewOpenCases = this.todayNewOpenCases.formatValue(),
        todayNewRecovered = this.todayNewRecovered.formatValue(),
        todayOpenCases = this.todayOpenCases.formatValue(),
        todayRecovered = this.todayRecovered.formatValue(),
        todayVsYesterdayConfirmed = (this.todayVsYesterdayConfirmed * 100).formatDecimals(),
        todayVsYesterdayDeaths = (this.todayVsYesterdayDeaths * 100).formatDecimals(),
        todayVsYesterdayOpenCases = (this.todayVsYesterdayOpenCases * 100).formatDecimals(),
        todayVsYesterdayRecovered = (this.todayVsYesterdayRecovered * 100).formatDecimals()
    )