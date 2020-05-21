package com.jaimegc.covid19tracker.ui.model

import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.common.extensions.formatDecimals
import com.jaimegc.covid19tracker.common.extensions.formatValue

fun CovidTracker.toUI(): CovidTrackerUI =
    CovidTrackerUI(
        countriesStats = countriesStats.map { country -> country.toUI() }.toList(),
        worldStats = worldStats.toUI()
    )

private fun CountryOneStats.toUI():  CountryStatsUI =
    CountryStatsUI(
        country = country.toUI(),
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

fun Stats.toChartUI(): StatsChartUI =
    StatsChartUI(
        date = date,
        source = source,
        confirmed = confirmed.toFloat(),
        deaths = deaths.toFloat(),
        newConfirmed = newConfirmed.toFloat(),
        newDeaths = newDeaths.toFloat(),
        newOpenCases = newOpenCases.toFloat(),
        newRecovered = newRecovered.toFloat(),
        openCases = openCases.toFloat(),
        recovered = recovered.toFloat()
    )

fun WorldStats.toListChartUI(): WorldStatsChartUI =
    WorldStatsChartUI(
        date = date,
        updatedAt = updatedAt,
        stats = StatsChartUI(
            date = date,
            source = stats.source,
            confirmed = stats.confirmed.toFloat(),
            deaths = stats.deaths.toFloat(),
            newConfirmed = stats.newConfirmed.toFloat(),
            newDeaths = stats.newDeaths.toFloat(),
            newOpenCases = stats.newOpenCases.toFloat(),
            newRecovered = stats.newRecovered.toFloat(),
            openCases = stats.openCases.toFloat(),
            recovered = stats.recovered.toFloat()
        )
    )

fun CountryAndStats.toListChartUI(): CountryListStatsChartUI =
    CountryListStatsChartUI(
        country = country.toUI(),
        stats = stats.map { statsCountry ->
            StatsChartUI(
                date = statsCountry.date,
                source = statsCountry.source,
                confirmed = statsCountry.confirmed.toFloat(),
                deaths = statsCountry.deaths.toFloat(),
                newConfirmed = statsCountry.newConfirmed.toFloat(),
                newDeaths = statsCountry.newDeaths.toFloat(),
                newOpenCases = statsCountry.newOpenCases.toFloat(),
                newRecovered = statsCountry.newRecovered.toFloat(),
                openCases = statsCountry.openCases.toFloat(),
                recovered = statsCountry.recovered.toFloat()
            )
        }
    )

fun CountryOneStats.toChartUI(): CountryStatsChartUI =
    CountryStatsChartUI(
        country = country.toUI(),
        stats = StatsChartUI(
            date = stats.date,
            source = stats.source,
            confirmed = stats.confirmed.toFloat(),
            deaths = stats.deaths.toFloat(),
            newConfirmed = stats.newConfirmed.toFloat(),
            newDeaths = stats.newDeaths.toFloat(),
            newOpenCases = stats.newOpenCases.toFloat(),
            newRecovered = stats.newRecovered.toFloat(),
            openCases = stats.openCases.toFloat(),
            recovered = stats.recovered.toFloat()
        )
    )

fun CovidTracker.toListChartUI(): List<WorldCountryStatsUI> =
    countriesStats.map { country ->
        WorldCountryStatsUI(country.toChartUI(), worldStats.toListChartUI())
    }

fun Country.toUI(): CountryUI =
    CountryUI(
        id = id,
        name = name,
        nameEs = nameEs,
        code = code
    )

fun Region.toPlaceUI(): PlaceUI =
    PlaceUI(
        id = id,
        name = name,
        nameEs = nameEs
    )

fun CountryOneStats.toPlaceUI(): PlaceStatsUI =
    PlaceStatsUI(
        id = country.id,
        name = country.name,
        nameEs = country.nameEs,
        code = country.code,
        stats = stats.toUI()
    )

fun RegionOneStats.toPlaceUI(): PlaceStatsUI =
    PlaceStatsUI(
        id = region.id,
        name = region.name,
        nameEs = region.nameEs,
        stats = stats.toUI()
    )

fun ListRegionStats.toPlaceUI(): List<PlaceStatsUI> =
    regionStats.map { regStats ->
        PlaceStatsUI(
            id = regStats.region.id,
            name = regStats.region.name,
            nameEs = regStats.region.nameEs,
            stats = regStats.stats.toUI()
        )
    }

fun ListSubRegionStats.toPlaceUI(): List<PlaceStatsUI> =
    subRegionStats.map { subRegStats ->
        PlaceStatsUI(
            id = subRegStats.subRegion.id,
            name = subRegStats.subRegion.name,
            nameEs = subRegStats.subRegion.nameEs,
            stats = subRegStats.stats.toUI()
        )
    }

fun ListRegionStats.toPlaceChartUI(): List<PlaceStatsChartUI> =
    regionStats.map { regStats ->
        PlaceStatsChartUI(
            place = PlaceUI(
                id = regStats.region.id,
                name = regStats.region.name,
                nameEs = regStats.region.nameEs
            ),
            stats = regStats.stats.toChartUI()
        )
    }

fun ListSubRegionStats.toPlaceChartUI(): List<PlaceStatsChartUI> =
    subRegionStats.map { subRegStats ->
        PlaceStatsChartUI(
            place = PlaceUI(
                id = subRegStats.subRegion.id,
                name = subRegStats.subRegion.name,
                nameEs = subRegStats.subRegion.nameEs
            ),
            stats = subRegStats.stats.toChartUI()
        )
    }

fun ListCountryStats.toPlaceUI(): List<StatsChartUI> =
    countriesStats.map { stats ->
        StatsChartUI(
            date = stats.date,
            source = stats.source,
            confirmed = stats.confirmed.toFloat(),
            deaths = stats.deaths.toFloat(),
            newConfirmed = stats.newConfirmed.toFloat(),
            newDeaths = stats.newDeaths.toFloat(),
            newOpenCases = stats.newOpenCases.toFloat(),
            newRecovered = stats.newRecovered.toFloat(),
            openCases = stats.openCases.toFloat(),
            recovered = stats.recovered.toFloat()
        )
    }

fun ListRegionAndStats.toPlaceUI(): List<PlaceListStatsChartUI> =
    regionStats.map { stats ->
        PlaceListStatsChartUI(
            place = stats.region.toPlaceUI(),
            stats = stats.stats.map { place -> place.toChartUI() }
        )
    }