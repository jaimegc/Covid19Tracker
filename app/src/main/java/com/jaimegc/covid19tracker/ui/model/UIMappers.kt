package com.jaimegc.covid19tracker.ui.model

import com.jaimegc.covid19tracker.common.extensions.formatCompactValue
import com.jaimegc.covid19tracker.common.extensions.formatValue
import com.jaimegc.covid19tracker.common.extensions.percentage
import com.jaimegc.covid19tracker.domain.model.Country
import com.jaimegc.covid19tracker.domain.model.CountryAndStats
import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListCountryOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListRegionOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegionStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionStats
import com.jaimegc.covid19tracker.domain.model.Region
import com.jaimegc.covid19tracker.domain.model.RegionOneStats
import com.jaimegc.covid19tracker.domain.model.Stats
import com.jaimegc.covid19tracker.domain.model.SubRegion
import com.jaimegc.covid19tracker.domain.model.WorldStats

fun CovidTracker.toUI(): CovidTrackerUI =
    CovidTrackerUI(
        countriesStats = countriesStats.map { country -> country.toUI() }.toList(),
        worldStats = worldStats.toUI()
    )

private fun CountryOneStats.toUI(): CountryStatsUI =
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
            confirmedCompact = stats.confirmed.formatCompactValue(),
            deathsCompact = stats.deaths.formatCompactValue(),
            openCasesCompact = stats.openCases.formatCompactValue(),
            recoveredCompact = stats.recovered.formatCompactValue(),
            vsYesterdayConfirmed = stats.vsYesterdayConfirmed.percentage(),
            vsYesterdayDeaths = stats.vsYesterdayDeaths.percentage(),
            vsYesterdayOpenCases = stats.vsYesterdayOpenCases.percentage(),
            vsYesterdayRecovered = stats.vsYesterdayRecovered.percentage()
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
        confirmedCompact = confirmed.formatCompactValue(),
        deathsCompact = deaths.formatCompactValue(),
        openCasesCompact = openCases.formatCompactValue(),
        recoveredCompact = recovered.formatCompactValue(),
        vsYesterdayConfirmed = vsYesterdayConfirmed.percentage(),
        vsYesterdayDeaths = vsYesterdayDeaths.percentage(),
        vsYesterdayOpenCases = vsYesterdayOpenCases.percentage(),
        vsYesterdayRecovered = vsYesterdayRecovered.percentage()
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
        WorldCountryStatsUI(
            countryStats = country.toChartUI(),
            worldStats = worldStats.toListChartUI()
        )
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

fun SubRegion.toPlaceUI(): PlaceUI =
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

fun ListCountryOnlyStats.toPlaceUI(): List<StatsChartUI> =
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

fun ListRegionOnlyStats.toPlaceUI(): List<StatsChartUI> =
    regionStats.map { stats ->
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

fun ListSubRegionAndStats.toPlaceUI(): List<PlaceListStatsChartUI> =
    subRegionStats.map { stats ->
        PlaceListStatsChartUI(
            place = stats.subRegion.toPlaceUI(),
            stats = stats.stats.map { place -> place.toChartUI() }
        )
    }

fun DomainError.toUI(): ErrorUI = ErrorUI.SomeError