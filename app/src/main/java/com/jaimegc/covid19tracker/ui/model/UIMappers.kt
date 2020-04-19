package com.jaimegc.covid19tracker.ui.model

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
        stats = stats.toUI()
    )

fun WorldStats.toUI(): WorldStatsUI =
    WorldStatsUI(
        date = date,
        updatedAt = updatedAt,
        stats = StatsUI(
            date = date,
            source = stats.source,
            confirmed = stats.confirmed.formatValue(),
            deaths = stats.deaths.formatValue(),
            newConfirmed = stats.newConfirmed.formatValue(),
            newDeaths = stats.newDeaths.formatValue(),
            newOpenCases = stats.newOpenCases.formatValue(),
            newRecovered = stats.newRecovered.formatValue(),
            openCases = stats.openCases.formatValue(),
            recovered = stats.recovered.formatValue(),
            vsYesterdayConfirmed = (stats.vsYesterdayConfirmed * 100).formatDecimals(),
            vsYesterdayDeaths = (stats.vsYesterdayDeaths * 100).formatDecimals(),
            vsYesterdayOpenCases = (stats.vsYesterdayOpenCases * 100).formatDecimals(),
            vsYesterdayRecovered = (stats.vsYesterdayRecovered * 100).formatDecimals()
        )
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