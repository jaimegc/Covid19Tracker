package com.jaimegc.covid19tracker.data.api.model

import com.google.gson.annotations.SerializedName

data class CovidTrackerDto(
    @SerializedName("dates") val dates: Map<String, CovidTrackerDateDto>,
    @SerializedName("total") val total: CovidTrackerTotalDto,
    @SerializedName("updated_at") val updatedAt: String
)

data class CovidTrackerDateDto(
    @SerializedName("countries") val countries: Map<String, CovidTrackerDateCountryDto>
)

data class CovidTrackerDateCountryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("name_es") val nameEs: String,
    @SerializedName("source") val source: String? = null,
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
    @SerializedName("yesterday_recovered") val yesterdayRecovered: Long,
    @SerializedName("regions") val regions: List<CovidTrackerDateCountryDto>? = null,
    @SerializedName("sub_regions") val subRegions: List<CovidTrackerDateCountryDto>? = null
)

data class CovidTrackerTotalDto(
    @SerializedName("source") val source: String? = null,
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