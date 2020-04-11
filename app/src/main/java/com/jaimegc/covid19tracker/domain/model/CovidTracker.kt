package com.jaimegc.covid19tracker.domain.model

import com.jaimegc.covid19tracker.extensions.formatDecimals
import com.jaimegc.covid19tracker.extensions.formatValue
import com.jaimegc.covid19tracker.ui.model.CovidTrackerTotalUI
import com.jaimegc.covid19tracker.ui.model.CovidTrackerUI

data class CovidTracker(
    val total: CovidTrackerTotal
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

fun CovidTracker.toUI(): CovidTrackerUI = CovidTrackerUI(total.toUI())

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