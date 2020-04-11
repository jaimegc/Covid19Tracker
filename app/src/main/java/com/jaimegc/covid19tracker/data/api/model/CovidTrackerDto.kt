package com.jaimegc.covid19tracker.data.api.model

import com.google.gson.annotations.SerializedName
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.CovidTrackerTotal

data class CovidTrackerDto(
    @SerializedName("total") val total: CovidTrackerTotalDto
)

data class CovidTrackerTotalDto(
    @SerializedName("date") val date: String,
    @SerializedName("source") val source: String,
    @SerializedName("today_confirmed") val todayConfirmed: Long,
    @SerializedName("today_deaths") val todayDeaths: Long,
    @SerializedName("today_new_confirmed") val todayNewConfirmed: Long,
    @SerializedName("today_new_deaths") val todayNewDeaths: Long,
    @SerializedName("today_new_open_cases") val todayNewOpenCases: Long,
    @SerializedName("today_new_recovered") val todayNewRecovered: Long,
    @SerializedName("today_open_cases") val todayOpenCases: Long,
    @SerializedName("today_recovered") val todayRecovered: Long,
    @SerializedName("today_vs_yesterday_confirmed") val todayVsYesterdayConfirmed: Double,
    @SerializedName("today_vs_yesterday_deaths") val todayVsYesterdayDeaths: Double,
    @SerializedName("today_vs_yesterday_open_cases") val todayVsYesterdayOpenCases: Double,
    @SerializedName("today_vs_yesterday_recovered") val todayVsYesterdayRecovered: Double,
    @SerializedName("yesterday_confirmed") val yesterdayConfirmed: Long,
    @SerializedName("yesterday_deaths") val yesterdayDeaths: Long,
    @SerializedName("yesterday_open_cases") val yesterdayOpenCases: Long,
    @SerializedName("yesterday_recovered") val yesterdayRecovered: Long
)

fun CovidTrackerDto.toDomain(): CovidTracker = CovidTracker(total.toDomain())

fun CovidTrackerTotalDto.toDomain(): CovidTrackerTotal =
    CovidTrackerTotal(
        date = this.date,
        source = this.source,
        todayConfirmed = this.todayConfirmed,
        todayDeaths = this.todayDeaths,
        todayNewConfirmed = this.todayNewConfirmed,
        todayNewDeaths = this.todayNewDeaths,
        todayNewOpenCases = this.todayNewOpenCases,
        todayNewRecovered = this.todayNewRecovered,
        todayOpenCases = this.todayOpenCases,
        todayRecovered = this.todayRecovered,
        todayVsYesterdayConfirmed = this.todayVsYesterdayConfirmed,
        todayVsYesterdayDeaths = this.todayVsYesterdayDeaths,
        todayVsYesterdayOpenCases = this.todayVsYesterdayOpenCases,
        todayVsYesterdayRecovered = this.todayVsYesterdayRecovered
    )