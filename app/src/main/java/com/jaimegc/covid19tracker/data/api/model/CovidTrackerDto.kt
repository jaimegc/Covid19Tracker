package com.jaimegc.covid19tracker.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CovidTrackerDto(
    @Json(name = "dates") val dates: Map<String, CovidTrackerDateDto>,
    @Json(name = "total") val total: CovidTrackerTotalDto,
    @Json(name = "updated_at") val updatedAt: String
)

@JsonClass(generateAdapter = true)
data class CovidTrackerDateDto(
    @Json(name = "countries") val countries: Map<String, CovidTrackerDateCountryDto>
)

@JsonClass(generateAdapter = true)
data class CovidTrackerDateCountryDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "name_es") val nameEs: String,
    @Json(name = "source") val source: String?,
    @Json(name = "today_confirmed") val todayConfirmed: Long?,
    @Json(name = "today_deaths") val todayDeaths: Long?,
    @Json(name = "today_new_confirmed") val todayNewConfirmed: Long?,
    @Json(name = "today_new_deaths") val todayNewDeaths: Long?,
    @Json(name = "today_new_open_cases") val todayNewOpenCases: Long?,
    @Json(name = "today_new_recovered") val todayNewRecovered: Long?,
    @Json(name = "today_open_cases") val todayOpenCases: Long?,
    @Json(name = "today_recovered") val todayRecovered: Long?,
    @Json(name = "today_vs_yesterday_confirmed") val todayVsYesterdayConfirmed: Double?,
    @Json(name = "today_vs_yesterday_deaths") val todayVsYesterdayDeaths: Double?,
    @Json(name = "today_vs_yesterday_open_cases") val todayVsYesterdayOpenCases: Double?,
    @Json(name = "today_vs_yesterday_recovered") val todayVsYesterdayRecovered: Double?,
    @Json(name = "yesterday_confirmed") val yesterdayConfirmed: Long?,
    @Json(name = "yesterday_deaths") val yesterdayDeaths: Long?,
    @Json(name = "yesterday_open_cases") val yesterdayOpenCases: Long?,
    @Json(name = "yesterday_recovered") val yesterdayRecovered: Long?,
    @Json(name = "regions") val regions: List<CovidTrackerDateCountryDto>? = null,
    @Json(name = "sub_regions") val subRegions: List<CovidTrackerDateCountryDto>? = null
)

@JsonClass(generateAdapter = true)
data class CovidTrackerTotalDto(
    @Json(name = "source") val source: String? = null,
    @Json(name = "today_confirmed") val todayConfirmed: Long?,
    @Json(name = "today_deaths") val todayDeaths: Long?,
    @Json(name = "today_new_confirmed") val todayNewConfirmed: Long?,
    @Json(name = "today_new_deaths") val todayNewDeaths: Long?,
    @Json(name = "today_new_open_cases") val todayNewOpenCases: Long?,
    @Json(name = "today_new_recovered") val todayNewRecovered: Long?,
    @Json(name = "today_open_cases") val todayOpenCases: Long?,
    @Json(name = "today_recovered") val todayRecovered: Long?,
    @Json(name = "today_vs_yesterday_confirmed") val todayVsYesterdayConfirmed: Double?,
    @Json(name = "today_vs_yesterday_deaths") val todayVsYesterdayDeaths: Double?,
    @Json(name = "today_vs_yesterday_open_cases") val todayVsYesterdayOpenCases: Double?,
    @Json(name = "today_vs_yesterday_recovered") val todayVsYesterdayRecovered: Double?,
    @Json(name = "yesterday_confirmed") val yesterdayConfirmed: Long?,
    @Json(name = "yesterday_deaths") val yesterdayDeaths: Long?,
    @Json(name = "yesterday_open_cases") val yesterdayOpenCases: Long?,
    @Json(name = "yesterday_recovered") val yesterdayRecovered: Long?
)