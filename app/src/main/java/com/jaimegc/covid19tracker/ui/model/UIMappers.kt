package com.jaimegc.covid19tracker.ui.model

import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import com.jaimegc.covid19tracker.domain.model.CountryStats
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.Stats
import com.jaimegc.covid19tracker.domain.model.WorldStats
import com.jaimegc.covid19tracker.extensions.formatDecimals
import com.jaimegc.covid19tracker.extensions.formatValue

fun CovidTracker.toUI(): CovidTrackerUI =
    CovidTrackerUI(
        countriesStats = countriesStats.map { country -> country.toUI() }.toList(),
        worldStats = worldStats.toUI()
    )

private fun CountryStats.toUI():  CountryStatsUI =
    CountryStatsUI(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        stats = stats.toUI()
    )

fun WorldStats.toUI(): WorldStatsUI =
    WorldStatsUI(
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

fun Stats.toUI(): StatsUI =
    StatsUI(
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
        vsYesterdayRecovered = (vsYesterdayRecovered * 100).formatDecimals()
    )

fun CountryStats.toEntity(covidTrackerDateFk: String): CountryStatsEntity =
    CountryStatsEntity(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        source = stats.source,
        confirmed = stats.confirmed,
        deaths = stats.deaths,
        newConfirmed = stats.newConfirmed,
        newDeaths = stats.newDeaths,
        newOpenCases = stats.newOpenCases,
        newRecovered = stats.newRecovered,
        openCases = stats.openCases,
        recovered = stats.recovered,
        vsYesterdayConfirmed = stats.vsYesterdayConfirmed,
        vsYesterdayDeaths = stats.vsYesterdayDeaths,
        vsYesterdayOpenCases = stats.vsYesterdayOpenCases,
        vsYesterdayRecovered = stats.vsYesterdayRecovered,
        dateWorldStatsFk = covidTrackerDateFk
    )

fun WorldStats.toEntity(): WorldStatsEntity =
    WorldStatsEntity(
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
        updatedAt = updatedAt
    )