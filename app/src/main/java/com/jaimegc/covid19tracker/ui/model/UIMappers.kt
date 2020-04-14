package com.jaimegc.covid19tracker.ui.model

import com.jaimegc.covid19tracker.data.room.entities.CountryTodayStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.CovidTrackerEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldTodayStatsEntity
import com.jaimegc.covid19tracker.domain.model.Country
import com.jaimegc.covid19tracker.domain.model.CountryStats
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.TodayStats
import com.jaimegc.covid19tracker.extensions.formatDecimals
import com.jaimegc.covid19tracker.extensions.formatValue

fun CovidTracker.toUI(): CovidTrackerUI =
    CovidTrackerUI(
        countryStats = countryStats.toUI(),
        worldStats = worldStats.toUI()
    )

fun CountryStats.toUI(): CountryStatsUI =
    CountryStatsUI(
        countries = countries.map { country -> country.toUI() }.toList()
    )

private fun Country.toUI():  CountryUI =
    CountryUI(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        todayStats = todayStats.toUI()
    )

fun TodayStats.toUI(): TodayStatsUI =
    TodayStatsUI(
        date = date,
        source = source,
        confirmed = confirmed.formatValue(),
        deaths = deaths.formatValue(),
        newConfirmed = newConfirmed.formatValue(),
        newDeaths = newDeaths.formatValue(),
        newOpenCases = newOpenCases.formatValue(),
        newRecovered = newRecovered.formatValue(),
        openCases = openCases.formatValue(),
        recovered = recovered.formatValue(),
        vsYesterdayConfirmed = (vsYesterdayConfirmed * 100).formatDecimals(),
        vsYesterdayDeaths = (vsYesterdayDeaths * 100).formatDecimals(),
        vsYesterdayOpenCases = (vsYesterdayOpenCases * 100).formatDecimals(),
        vsYesterdayRecovered = (vsYesterdayRecovered * 100).formatDecimals(),
        updatedAt = updatedAt
    )

fun CovidTracker.toEntity(): CovidTrackerEntity =
    CovidTrackerEntity(
        date = date,
        updateAt = updatedAt
    )

fun Country.toEntity(covidTrackerDateFk: String): CountryTodayStatsEntity =
    CountryTodayStatsEntity(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        source = todayStats.source,
        confirmed = todayStats.confirmed,
        deaths = todayStats.deaths,
        newConfirmed = todayStats.newConfirmed,
        newDeaths = todayStats.newDeaths,
        newOpenCases = todayStats.newOpenCases,
        newRecovered = todayStats.newRecovered,
        openCases = todayStats.openCases,
        recovered = todayStats.recovered,
        vsYesterdayConfirmed = todayStats.vsYesterdayConfirmed,
        vsYesterdayDeaths = todayStats.vsYesterdayDeaths,
        vsYesterdayOpenCases = todayStats.vsYesterdayOpenCases,
        vsYesterdayRecovered = todayStats.vsYesterdayRecovered,
        updatedAt = todayStats.updatedAt,
        dateCovidTrackerFk = covidTrackerDateFk
    )

fun TodayStats.toWorldEntity(covidTrackerDateFk: String): WorldTodayStatsEntity =
    WorldTodayStatsEntity(
        date = date,
        source = source,
        confirmed = confirmed,
        deaths = deaths,
        newConfirmed = newConfirmed,
        newDeaths = newDeaths,
        newOpenCases = newOpenCases,
        newRecovered = newRecovered,
        openCases = openCases,
        recovered = recovered,
        vsYesterdayConfirmed = vsYesterdayConfirmed,
        vsYesterdayDeaths = vsYesterdayDeaths,
        vsYesterdayOpenCases = vsYesterdayOpenCases,
        vsYesterdayRecovered = vsYesterdayRecovered,
        updatedAt = updatedAt,
        dateCovidTrackerFk = covidTrackerDateFk
    )